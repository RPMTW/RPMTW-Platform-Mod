package com.rpmtw.rpmtw_platform_mod.quilt

import com.rpmtw.rpmtw_platform_mod.fabriclike.RPMTWPlatformModFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

class RPMTWPlatformModQuilt : ModInitializer {
    override fun onInitialize(mod: ModContainer?) {
        RPMTWPlatformModFabricLike.init()
    }
}