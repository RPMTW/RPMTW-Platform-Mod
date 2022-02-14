package com.rpmtw.rpmtw_platform_mod.fabric;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import net.fabricmc.api.ModInitializer;

public class RPMTWPlatformModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RPMTWPlatformMod.init();
    }
}
