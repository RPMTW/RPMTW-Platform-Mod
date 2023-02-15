package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTStorage
import net.minecraft.server.packs.resources.ResourceManager

object OnReloadLanguage {
    fun onReload(resourceManager: ResourceManager) {
        RPMTWPlatformMod.LOGGER.info("Reloading language...")
        MTStorage.load(resourceManager)
    }
}