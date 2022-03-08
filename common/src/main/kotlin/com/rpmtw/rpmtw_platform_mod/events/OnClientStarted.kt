package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.LanguageInfo
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

@Environment(EnvType.CLIENT)
class OnClientStarted(val client: Minecraft) {
    init {
        // RPMTWConfig.register()
        toggleLanguage()
        CosmicChatHandler.handle()
        MTManager.readCache()
        timer(initialDelay = TimeUnit.SECONDS.toMillis(5), period = TimeUnit.MINUTES.toMillis(5)) {
            Utilities.coroutineLaunch {
                // Auto save cache in every 5 minutes
                MTManager.saveCache()
            }
        }
    }

    private fun toggleLanguage() {
        val minecraft = Minecraft.getInstance()
        val langCode = Utilities.languageCode
        val manager = minecraft.languageManager

        // if auto-toggle language is enabled and the user language is Chinese, set the language
        if (langCode == "zh_tw") {
            // set the language to Traditional Chinese
            manager.selected = LanguageInfo("zh_tw", "TW", "繁體中文", false)
        } else if (langCode == "zh_cn") {
            // set language to Simplified Chinese
            manager.selected = LanguageInfo("zh_cn", "CN", "简体中文", false)
        }

        val info = manager.selected
        RPMTWPlatformMod.LOGGER.info("Toggled language to " + info.name + " (" + info.code + ")")
    }
}