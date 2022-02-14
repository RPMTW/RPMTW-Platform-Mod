package com.rpmtw.rpmtw_platform_mod.forge;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class RPMTWPlatformModPluginImpl {
    /**
     * This is our actual method to {@link RPMTWPlatformModPlugin#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
