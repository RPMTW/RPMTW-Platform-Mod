package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import dev.architectury.injectables.targets.ArchitecturyTarget
import dev.architectury.platform.Platform
import io.sentry.Sentry
import io.sentry.protocol.User
import net.minecraft.client.Minecraft

object SentryHandler {
    fun init() {
        RPMTWPlatformMod.LOGGER.info("Initializing Sentry")

        Sentry.init { options ->
            options.dsn = "https://8ce62fbbd519458d95abe1115b811e8c@o1068024.ingest.sentry.io/6633879"

            if (!Platform.isDevelopmentEnvironment()) {
                options.release = RPMTWPlatformMod.VERSION
            }
            options.tracesSampleRate = 1.0
        }

        Sentry.configureScope {
            it.user = User().apply {
                username = Minecraft.getInstance().user.name
                ipAddress = "{{auto}}"
            }

            it.setContexts(
                "mod_info", mapOf(
                    "minecraft_version" to Platform.getMinecraftVersion(),
                    "mod_version" to RPMTWPlatformMod.VERSION,
                    "mod_loader" to ArchitecturyTarget.getCurrentTarget()
                )
            )
        }
    }
}