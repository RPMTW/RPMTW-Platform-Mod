package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.translation.GameLanguage
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.LanguageInfo
import java.util.*

@Environment(EnvType.CLIENT)
class OnClientStarted : ClientLifecycleEvent.ClientState {
    override fun stateChanged(instance: Minecraft) {
        toggleLanguage(instance)
        UniverseChatHandler.handle()
        MTManager.readCache()
    }

    private fun toggleLanguage(instance: Minecraft) {
        if (!RPMTWConfig.get().translate.autoToggleLanguage) return

        val manger = instance.languageManager
        val language = GameLanguage.getSystem()

        if (language == null || language == GameLanguage.English) return

        manger.selected = LanguageInfo(language.code, "", "", false)
        RPMTWPlatformMod.LOGGER.info("Auto toggle language to ${language.code}(${language.isO3Code})")
    }
}