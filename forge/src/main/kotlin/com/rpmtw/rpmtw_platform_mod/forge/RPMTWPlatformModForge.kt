package com.rpmtw.rpmtw_platform_mod.forge

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod.init
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod(RPMTWPlatformMod.MOD_ID)
class RPMTWPlatformModForge {
    init {
        EventBuses.registerModEventBus(RPMTWPlatformMod.MOD_ID, MOD_CONTEXT.getKEventBus())
        MOD_BUS.addListener(this::init)
    }

    private fun init(event: FMLCommonSetupEvent) {
        init()
    }

}