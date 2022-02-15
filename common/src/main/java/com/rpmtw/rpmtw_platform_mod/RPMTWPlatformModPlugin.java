package com.rpmtw.rpmtw_platform_mod;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;

import java.nio.file.Path;

public class RPMTWPlatformModPlugin {
    /**
     * We can use {@link Platform#getConfigFolder()} but this is just an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin#getConfigDirectory()
     * Actual Fabric: com.rpmtw.rpmtw_platform_mod.fabric.RPMTWPlatformModPluginImpl#getConfigDirectory()
     * Actual Forge: com.rpmtw.rpmtw_platform_mod.forge.RPMTWPlatformModPluginImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
