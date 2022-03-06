package com.rpmtw.rpmtw_platform_mod.forge

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.minecraftforge.fml.common.Mod

@Mod(RPMTWPlatformMod.MOD_ID)
object ForgeKotlinClient {
    init {
        RPMTWPlatformMod.init()
    }
}