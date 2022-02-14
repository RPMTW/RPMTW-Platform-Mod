package com.rpmtw.rpmtw_platform_mod.fabric;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class RPMTWPlatformModPluginImpl {
    /**
     * This is our actual method to {@link RPMTWPlatformModPlugin#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
