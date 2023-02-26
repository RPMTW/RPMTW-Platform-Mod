package com.rpmtw.rpmtw_platform_mod.translation.resourcepack

import com.google.gson.Gson
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL

object TranslateResourcePack {
    private const val fileName = "RPMTW-Translate-Resource-Pack-1.18.zip"
    private val resourcePackFolder: File = RPMTWPlatformModPlugin.getGameFolder().resolve("resourcepacks")
    private val resourcePackFile = resourcePackFolder.resolve(fileName)
    private val cacheFile = Util.getFileLocation(fileName)
    private var loaded = false

    fun init() {
        deleteResourcePack()
        if (!RPMTWConfig.get().translate.loadTranslateResourcePack || loaded) return

        try {
            load()
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to set translate resource pack", e)
            return
        }
    }

    fun reload() {
        load()
        Minecraft.getInstance().reloadResourcePacks()
    }

    private fun load() {
        try {
            download()
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to download translate resource pack", e)
            return
        }

        try {
            cacheFile.copyTo(resourcePackFile, true)
            resourcePackFile.deleteOnExit()
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to copy translate resource pack", e)
            return
        }

        selectResourcePack()

        loaded = true
        RPMTWPlatformMod.LOGGER.info("Translate resource pack successful loaded")
    }

    private fun deleteResourcePack() {
        if (resourcePackFile.exists()) {
            resourcePackFile.delete()
        }
    }

    private fun download() {
        // Check resource pack folder exists
        if (!resourcePackFolder.exists() || !resourcePackFolder.isDirectory) {
            resourcePackFolder.mkdir()
        }

        val time = 1000 * 60 * 30 // 30 minutes
        // If cache file exists and is not older than 30 minutes, use cache file
        if (cacheFile.exists() && cacheFile.isFile && cacheFile.lastModified() > System.currentTimeMillis() - (time)) {
            return
        }

        val downloadUrl = "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip"

        FileUtils.copyURLToFile(URL(downloadUrl), cacheFile)
        if (!cacheFile.exists()) {
            throw Exception("File not found")
        }
    }

    private fun selectResourcePack() {
        val client = Minecraft.getInstance()
        val optionsFile = client.gameDirectory.resolve("options.txt")
        val options = mutableMapOf<String, String>()

        if (optionsFile.exists()) {
            runCatching {
                FileUtils.readLines(optionsFile, StandardCharsets.UTF_8)
                    .map { it.split(Regex(":"), 2) }
                    .filter { it.size == 2 }
                    .forEach { options[it[0]] = it[1] }
            }
        }

        val selected = Gson().fromJson(options["resourcePacks"] ?: "[]", Array<String>::class.java).toMutableList()
        val packId = getPackId()

        // If the pack is unselected, select it
        if (packId !in selected) {
            // Set the pack last in the list (the highest priority)
            selected.add(packId)
        }

        options["resourcePacks"] = Gson().toJson(selected)
        FileUtils.writeLines(optionsFile, options.map { "${it.key}:${it.value}" })
    }

    fun getPackId(): String {
        return "file/$fileName"
    }
}