@file:JvmName("ClientInit")
package com.rpmtw.rpmtw_platform_mod.mixins

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.main.GameConfig
import net.minecraft.client.resources.language.LanguageInfo
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.*

@Suppress("UNUSED_PARAMETER")
@Mixin(Minecraft::class)
class ClientInit {
    @Inject(method = ["<init>"], at = [At("RETURN")])
    fun init(gameConfig: GameConfig?, ci: CallbackInfo?) {
        //  RPMTWConfig.register()
        val minecraft = Minecraft.getInstance()
        val langCode = Locale.getDefault().language
        // if auto-toggle language is enabled and the user language is Chinese, set the language
        if (RPMTWConfig.get().translate.autoToggleLanguage && (langCode.contains("zh") || langCode.contains("chi"))) {
            val countryCode = Locale.getDefault().country
            val manager = minecraft.languageManager
            if (countryCode.contains("TW") || countryCode.contains("HK")) {
                // set the language to Traditional Chinese
                manager.selected = LanguageInfo("zh_tw", "TW", "繁體中文", false)
            } else if (countryCode.contains("CN")) {
                // set language to Simplified Chinese
                manager.selected = LanguageInfo("zh_cn", "CN", "简体中文", false)
            }
            val info = manager.selected
            RPMTWPlatformMod.LOGGER.info("Toggled language to " + info.name + " (" + info.code + ")")
        }
    }
}