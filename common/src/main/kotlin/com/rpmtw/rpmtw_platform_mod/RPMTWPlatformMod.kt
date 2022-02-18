package com.rpmtw.rpmtw_platform_mod

import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object RPMTWPlatformMod {
    const val MOD_ID: String = "rpmtw_platform_mod"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID) // register logger

    @JvmStatic
    fun init() {
        RPMTWConfig.register()
    }
}