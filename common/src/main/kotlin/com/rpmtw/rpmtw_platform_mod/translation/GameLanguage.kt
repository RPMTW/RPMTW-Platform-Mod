package com.rpmtw.rpmtw_platform_mod.translation

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.minecraft.client.Minecraft
import java.util.*

enum class GameLanguage(val code: String, val region: String, val codeList: List<String>) {
    English("en_us", "US", arrayListOf("US", "en")),
    TraditionalChinese("zh_tw", "TW", arrayListOf("TW", "TWN", "HK", "HKG")),
    SimplifiedChinese("zh_cn", "CN", arrayListOf("CN"));

    companion object {
        /*
         * Get the system language
         */
        fun getSystem(): GameLanguage {
            val locale = Locale.getDefault()
            val countryCode = locale.country
            val languageCode = locale.language

            RPMTWPlatformMod.LOGGER.debug("System language: $languageCode-$countryCode")

            return findLanguage(countryCode) ?: findLanguage(languageCode) ?: English
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

        private fun findLanguage(code: String): GameLanguage? {
            val languages = values()
            for (language in languages) {
                if (language.codeList.contains(code) && language != English) {
                    return language
                }
            }
            return null
        }
    }
}