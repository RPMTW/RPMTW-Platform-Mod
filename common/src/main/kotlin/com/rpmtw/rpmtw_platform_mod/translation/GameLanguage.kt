package com.rpmtw.rpmtw_platform_mod.translation

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.minecraft.client.Minecraft
import java.util.*

enum class GameLanguage(val code: String, val region: String, val tags: List<String>) {
    English("en_us", "US", arrayListOf("en-US")),
    TraditionalChinese("zh_tw", "TW", arrayListOf("zh-TW", "zh-HK")),
    SimplifiedChinese("zh_cn", "CN", arrayListOf("zh-CN", "zh-SG"));

    companion object {
        /*
         * Get the system language
         */
        fun getSystem(): GameLanguage {
            val locale = Locale.getDefault()
            val tag = locale.toLanguageTag()

            RPMTWPlatformMod.LOGGER.debug("Language tag: $tag")

            return when {
                English.tags.contains(tag) -> English
                TraditionalChinese.tags.contains(tag) -> TraditionalChinese
                SimplifiedChinese.tags.contains(tag) -> SimplifiedChinese
                else -> English
            }
        }

        /**
         * Get the game language.
         *
         * If the result is [GameLanguage.English], we will return the [getSystem].
         */
        fun getMinecraft(): GameLanguage {
            val languages = values()
            val systemLanguage = getSystem()
            val mcLanguage = Minecraft.getInstance().languageManager.selected.code

            var result: GameLanguage? = null

            for (language in languages) {
                if (language.code == mcLanguage) {
                    result = language
                    break
                }
            }

            if (result == English && systemLanguage != English) {
                result = systemLanguage
            }

            return result ?: English
        }
    }
}