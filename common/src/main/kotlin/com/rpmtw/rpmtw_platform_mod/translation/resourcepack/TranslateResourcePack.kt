package com.rpmtw.rpmtw_platform_mod.translation.resourcepack

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.translation.TranslateLanguage
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.Paths

object TranslateResourcePack {
    private const val fileName = "RPMTW-Translate-Resource-Pack-1.19.zip"
    private val resourcePackFolder: File = RPMTWPlatformModPlugin.getResourcePacksFolder()
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

        try {
            selectResourcePack()
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to sed translate resource pack", e)
            return
        }
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

        var downloadUrl = "https://github.com/RPMTW/Translate-Resource-Pack/releases/latest/download/$fileName";
        if (TranslateLanguage.getLanguage() == TranslateLanguage.SimplifiedChinese) {
            downloadUrl = "https://gh.api.99988866.xyz/${downloadUrl}"
        }

        FileUtils.copyURLToFile(URL(downloadUrl), cacheFile)
        if (!cacheFile.exists()) {
            throw Exception("File not found")
        }
    }

    private fun selectResourcePack() {
        val client = Minecraft.getInstance()
        val repository = client.resourcePackRepository
        val packID = "file/$fileName"
        // if the pack is unselected, select it
        if (!repository.selectedIds.contains(fileName)) {
            // Set the pack last in the list (the highest priority)
            repository.reload()
            val selected: MutableList<String> = repository.selectedIds.toMutableList()
            val pack = repository.getPack(packID)
            if (pack != null) {
                selected.add(pack.id)
            } else {
                throw Exception("Translate resource pack not found")
            }
            repository.setSelected(selected)
            client.reloadResourcePacks()
        }
    }
}