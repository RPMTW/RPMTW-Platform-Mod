package com.rpmtw.rpmtw_platform_mod.forge

import net.minecraftforge.fml.common.Mod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod

@Mod(RPMTWPlatformMod.MOD_ID)
object ForgeKotlinClient {
    init {
        RPMTWPlatformMod.init()
    }
}