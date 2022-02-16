package com.rpmtw.rpmtw_platform_mod;

import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RPMTWPlatformMod {
    public static final String MOD_ID = "rpmtw_platform_mod";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID); // register logger

    public static void init() {
        RPMTWConfig.register();
    }
}
