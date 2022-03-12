package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import dev.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.LanguageInfo

@Environment(EnvType.CLIENT)
class OnClientStarted() : ClientLifecycleEvent.ClientState {
    private fun toggleLanguage(instance: Minecraft) {
        val langCode = Utilities.languageCode
        val manager = instance.languageManager

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

    override fun stateChanged(instance: Minecraft?) {
        if (instance != null) {
            RPMTWConfig.register()
            CosmicChatHandler.handle()
            toggleLanguage(instance)
            MTManager.readCache()
        }
    }
}