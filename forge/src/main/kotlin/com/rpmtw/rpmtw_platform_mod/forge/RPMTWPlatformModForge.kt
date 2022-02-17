package com.rpmtw.rpmtw_platform_mod.forge

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod.init
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(RPMTWPlatformMod.MOD_ID)
class RPMTWPlatformModForge {
    init {
        EventBuses.registerModEventBus(RPMTWPlatformMod.MOD_ID, FMLJavaModLoadingContext.get().modEventBus)
        init()
    }
}