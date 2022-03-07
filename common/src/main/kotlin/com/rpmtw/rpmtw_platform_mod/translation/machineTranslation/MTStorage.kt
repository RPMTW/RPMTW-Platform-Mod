package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

class MTStorage : SimplePreparableReloadListener<CompletableFuture<Void>>() {
    private val unsupportedFormatRegx: Regex = Regex("%(\\d+\\$)?[\\d.]*[df]")

    companion object {
        private var unlocalizedMap: MutableMap<String, String> = HashMap()

        fun getUnlocalizedTranslate(key: String): String? {
            return unlocalizedMap[key]
        }
    }

    override fun prepare(manager: ResourceManager, profilerFiller: ProfilerFiller): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            if (RPMTWConfig.get().translate.unlocalized || RPMTWConfig.get().translate.machineTranslation) {

                for (namespace in manager.namespaces) {
                    val path = "lang/en_us.json"
                    try {
                        val identifier = ResourceLocation(namespace, path)
                        val resourceList: List<Resource> = manager.getResources(identifier)
                        for (resource in resourceList) {
                            val gson = Gson()
                            val jsonObject: JsonObject = gson.fromJson(
                                InputStreamReader(resource.inputStream, StandardCharsets.UTF_8),
                                JsonObject::class.java
                            )
                            for ((key, value1) in jsonObject.entrySet()) {
                                val value: String =
                                    unsupportedFormatRegx.replace(GsonHelper.convertToString(value1, key), "%$1s")

                                unlocalizedMap[key] = value
                            }
                        }
                    } catch (ignored: FileNotFoundException) {
                    } catch (e: Exception) {
                        RPMTWPlatformMod.LOGGER.warn(
                            "[Machine Translation] Skipped language file: {}:{} ({})",
                            namespace,
                            path,
                            e.toString()
                        )
                    }
                }
            }
        }
    }

    override fun apply(
        `object`: CompletableFuture<Void>,
        resourceManager: ResourceManager,
        profilerFiller: ProfilerFiller
    ) {
        // no-op
    }
}