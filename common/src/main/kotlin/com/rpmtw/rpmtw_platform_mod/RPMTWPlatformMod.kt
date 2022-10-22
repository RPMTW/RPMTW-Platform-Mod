package com.rpmtw.rpmtw_platform_mod

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_platform_mod.command.RPMTWCommand
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.handlers.EventHandler
import com.rpmtw.rpmtw_platform_mod.handlers.SentryHandler
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import dev.architectury.platform.Platform
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object RPMTWPlatformMod {
    const val MOD_ID = "rpmtw_platform_mod"
    val VERSION: String = Platform.getMod(MOD_ID).version

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID) // register logger

    @JvmStatic
    fun init() {
        LOGGER.info("Initializing RPMTW Platform Mod")
        RPMTWConfig.register()
        if (RPMTWConfig.get().advanced.sendExceptionToSentry) {
            SentryHandler.init()
        }
        RPMTWApiClient.init()
        TranslateResourcePack.deleteResourcePack()
        RPMTWCommand.handle()
        EventHandler.handle()
    }
}