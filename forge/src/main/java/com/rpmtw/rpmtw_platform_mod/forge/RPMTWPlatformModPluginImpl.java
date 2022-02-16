package com.rpmtw.rpmtw_platform_mod.forge;


import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class RPMTWPlatformModPluginImpl {
    public static void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> RPMTWConfig.getScreen(screen)));
    }
}
