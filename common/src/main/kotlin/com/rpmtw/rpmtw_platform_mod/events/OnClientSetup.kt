package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
class OnClientSetup {
    init {
        if (RPMTWConfig.get().translate.loadTranslateResourcePack) {
            TranslateResourcePack.load()
        }
    }
}