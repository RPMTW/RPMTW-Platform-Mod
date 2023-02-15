package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import dev.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import java.util.Locale

@Environment(EnvType.CLIENT)
class OnClientSetup : ClientLifecycleEvent.ClientState {
    override fun stateChanged(instance: Minecraft?) {
        if (instance == null) return
        if (!RPMTWConfig.get().translate.autoToggleLanguage) return

        val manger = instance.languageManager
        val country = Locale.getDefault().isO3Country
        val countryToLanguageMap = mapOf(
            Pair("TWN", "zh_tw"),
            Pair("CHN", "zh_cn"),
            Pair("HKG", "zh_hk"),
        )
        val languageCode = countryToLanguageMap[country] ?: return
        manger.selected = manger.getLanguage(languageCode)

        RPMTWPlatformMod.LOGGER.info("Auto toggle language to $languageCode($country)")

        if (RPMTWConfig.get().translate.loadTranslateResourcePack) {
            try {
                // If the resource pack is not loaded, it will be loaded.
                if (!TranslateResourcePack.loaded) {
                    TranslateResourcePack.load()
                }
            } catch (e: Exception) {
                RPMTWPlatformMod.LOGGER.error("Failed to set translate resource pack", e)
                return
            }
        }
    }
}