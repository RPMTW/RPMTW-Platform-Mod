package com.rpmtw.rpmtw_platform_mod;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class RPMTWPlatformModPlugin {
    @ExpectPlatform
    public static void registerConfig() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
