package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.translation.GameLanguage
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import dev.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.LanguageInfo

@Environment(EnvType.CLIENT)
class OnClientStarted : ClientLifecycleEvent.ClientState {
    /**
     * If auto-toggle language is enabled, will set language to the user's system language.
     */
    private fun toggleLanguage(instance: Minecraft) {
        val language = GameLanguage.getSystem()
        val manager = instance.languageManager

        manager.selected = LanguageInfo(language.code, language.region, language.name, false)

        val info = manager.selected
        RPMTWPlatformMod.LOGGER.info("Auto Toggled language to ${info.name} (${info.code}/${info.region})")
    }

    override fun stateChanged(instance: Minecraft?) {
        if (instance != null) {
            if (RPMTWConfig.get().translate.autoToggleLanguage) {
                toggleLanguage(instance)
            }
            UniverseChatHandler.handle()
            MTManager.readCache()
        }
    }
}