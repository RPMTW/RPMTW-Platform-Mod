package com.rpmtw.rpmtw_platform_mod

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_platform_mod.command.CommandHandler
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.handlers.EventHandler
import com.rpmtw.rpmtw_platform_mod.handlers.SentryHandler
import dev.architectury.platform.Platform
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object RPMTWPlatformMod {
    const val MOD_ID = "rpmtw_platform_mod"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    val VERSION: String
        get() = Platform.getMod(MOD_ID).version

    @JvmStatic
    fun init() {
        LOGGER.info("Initializing RPMTW Platform Mod")
        if (RPMTWConfig.get().advanced.sendExceptionToSentry) {
            SentryHandler.init()
        }
        RPMTWApiClient.init()
        CommandHandler.init()
        EventHandler.handle()
        RPMTWPlatformModPlugin.registerConfigScreen()
    }
}