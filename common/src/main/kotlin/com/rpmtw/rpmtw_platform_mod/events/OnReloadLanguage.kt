package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTStorage
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import net.minecraft.server.packs.resources.ResourceManager

object OnReloadLanguage {
    fun onReload(resourceManager: ResourceManager) {
        RPMTWPlatformMod.LOGGER.info("Reloading language...")
        if (RPMTWConfig.get().translate.loadTranslateResourcePack) {
            try {
                // If the resource pack is not loaded, it will be loaded.
                if (!TranslateResourcePack.loaded) {
                    TranslateResourcePack.load()
                }
            } catch (e: Exception) {
                RPMTWPlatformMod.LOGGER.error("Failed to set translate resource pack", e)
                return
            }
        }
        MTStorage.load(resourceManager)
    }
}