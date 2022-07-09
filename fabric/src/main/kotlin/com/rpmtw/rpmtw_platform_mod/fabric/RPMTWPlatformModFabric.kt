package com.rpmtw.rpmtw_platform_mod.fabric

import com.rpmtw.rpmtw_platform_mod.fabriclike.RPMTWPlatformModFabricLike
import net.fabricmc.api.ModInitializer

class RPMTWPlatformModFabric : ModInitializer {
    override fun onInitialize() {
        RPMTWPlatformModFabricLike.init()
    }
}