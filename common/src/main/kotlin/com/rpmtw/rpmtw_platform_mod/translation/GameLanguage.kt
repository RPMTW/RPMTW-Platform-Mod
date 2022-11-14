package com.rpmtw.rpmtw_platform_mod.translation

import net.minecraft.client.Minecraft

enum class GameLanguage(val code: String, val allowCodes: List<String>) {
    English("en_us", arrayListOf("en_us")),
    TraditionalChinese(
        "zh_tw",
        arrayListOf("zh_tw", "zh_hk", "lzh")
    ),
    SimplifiedChinese("zh_cn", arrayListOf("zh_cn"));

    companion object {
        /**
         * Get the game language.
         */
        fun getMinecraft(): GameLanguage {
            val languages = values()
            val mcLanguage = Minecraft.getInstance().options.languageCode

            for (language in languages) {
                if (mcLanguage in language.allowCodes) {
                    return language
                }
            }

            return English
        }
    }
}