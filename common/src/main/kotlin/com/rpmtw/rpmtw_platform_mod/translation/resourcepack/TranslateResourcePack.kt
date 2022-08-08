package com.rpmtw.rpmtw_platform_mod.translation.resourcepack

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.Paths

object TranslateResourcePack {
    private const val fileName = "RPMTW-Translate-Resource-Pack-1.19.zip"
    private val resourcePackFolder: File =
        File(Paths.get(Minecraft.getInstance().gameDirectory.path, "resourcepacks").toUri())
    private val resourcePackFile = resourcePackFolder.resolve(fileName)
    private val cacheFile = Util.getFileLocation(fileName)

    fun load() {
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

        RPMTWPlatformMod.LOGGER.info("Translate resource pack successful loaded")
    }

    fun reload() {
        load()
        Minecraft.getInstance().reloadResourcePacks()
        RPMTWPlatformMod.LOGGER.info("Translate resource pack successful reloaded")
    }

    fun deleteResourcePack() {
        if (resourcePackFile.exists()) {
            resourcePackFile.delete()
        }
    }

    private fun download() {
        // check resource pack folder exists
        if (!resourcePackFolder.exists() || !resourcePackFolder.isDirectory) {
            resourcePackFolder.mkdir()
        }

        val time = 1000 * 60 * 30 // 30 minutes
        // if cache file exists and is not older than 30 minutes, use cache file
        if (cacheFile.exists() && cacheFile.isFile && cacheFile.lastModified() > System.currentTimeMillis() - (time)) {
            return
        }

        val downloadUrl = "https://github.com/RPMTW/Translate-Resource-Pack/releases/latest/download/$fileName";

        FileUtils.copyURLToFile(URL(downloadUrl), cacheFile)
        if (!cacheFile.exists()) {
            throw Exception("File not found")
        }
    }

    fun selectResourcePack() {
        val client = Minecraft.getInstance()
        val repository = client.resourcePackRepository
        val packID = "file/$fileName"
        // if the pack is unselected, select it
        if (!repository.selectedIds.contains(packID)) {
            // Set the pack last in the list (the highest priority)
            val selected = repository.selectedIds.toMutableList()
            val pack = repository.getPack(packID)
            if (pack != null) {
                selected.add(pack.id)
            } else {
                throw Exception("Translate resource pack not found")
            }
            repository.setSelected(selected)
        }
    }
}