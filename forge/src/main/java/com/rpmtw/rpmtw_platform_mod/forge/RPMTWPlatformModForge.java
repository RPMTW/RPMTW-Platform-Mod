package com.rpmtw.rpmtw_platform_mod.forge;

import dev.architectury.platform.forge.EventBuses;
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RPMTWPlatformMod.MOD_ID)
public class RPMTWPlatformModForge {
    public RPMTWPlatformModForge() {
        EventBuses.registerModEventBus(RPMTWPlatformMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        RPMTWPlatformMod.init();
    }
}
