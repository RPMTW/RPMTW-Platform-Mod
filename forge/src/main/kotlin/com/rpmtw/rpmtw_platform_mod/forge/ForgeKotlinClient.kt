package com.rpmtw.rpmtw_platform_mod.forge

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod as common

class ForgeKotlinClient {
    fun init(event: FMLCommonSetupEvent) {
        common.init()
    }

}