package com.rpmtw.rpmtw_platform_mod.forge;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(RPMTWPlatformMod.MOD_ID)
public class RPMTWPlatformModForge {
    public RPMTWPlatformModForge() {
        MinecraftForge.EVENT_BUS.addListener(this::setup);
    }


    void setup(FMLCommonSetupEvent event) {
        ForgeKotlinClient forgeClient = new ForgeKotlinClient();
        forgeClient.init(event);
    }
}