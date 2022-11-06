package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.GameLanguage
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object MTStorage {
    private val unsupportedFormatRegx: Regex = Regex("%(\\d+\\$)?[\\d.]*[df]")

    private var unlocalizedMap: MutableMap<String, String> = HashMap()
    private var currentLanguageKeys: MutableList<String> = ArrayList()

    fun getUnlocalizedTranslate(key: String): String? {
        return unlocalizedMap[key]
    }

    fun isTranslate(key: String): Boolean {
        return key in currentLanguageKeys
    }

    fun load(manager: ResourceManager) {
        Util.coroutineLaunch {
            RPMTWPlatformMod.LOGGER.info("[Machine Translation] Loading resources...")
            val currentLanguage = GameLanguage.getMinecraft()
            unlocalizedMap.clear()
            currentLanguageKeys.clear()

            if (RPMTWConfig.get().translate.unlocalized || RPMTWConfig.get().translate.machineTranslation) {
                for (namespace in manager.namespaces) {
                    loadResource("en_us", { key, value ->
                        unlocalizedMap[key] = value
                    }, namespace, manager)

                    if (currentLanguage != GameLanguage.English) {
                        // Only load current language
                        loadResource(currentLanguage.code, { key, _ ->
                            currentLanguageKeys.add(key)
                        }, namespace, manager)
                    }
                }
            }
        }
    }

    private fun loadResource(
        lang: String,
        action: (key: String, value: String) -> Unit,
        namespace: String,
        manager: ResourceManager
    ) {
        val path = "lang/$lang.json"
        try {
            val identifier = ResourceLocation(namespace, path)
            val resourceList: List<Resource> = manager.getResourceStack(identifier)
            for (resource in resourceList) {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(
                    InputStreamReader(resource.open(), StandardCharsets.UTF_8),
                    JsonObject::class.java
                )
                for ((key, value1) in jsonObject.entrySet()) {
                    val value: String =
                        unsupportedFormatRegx.replace(GsonHelper.convertToString(value1, key), "%$1s")
                    action(key, value)
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