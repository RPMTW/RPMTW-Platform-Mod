package com.rpmtw.rpmtw_platform_mod.translation

import net.minecraft.client.Minecraft
import java.util.*

enum class GameLanguage(val code: String, val isO3Code: Set<String>) {
    English("en_us", setOf("ENG")),
    TraditionalChinese("zh_tw", setOf("TWN", "HKG")),
    SimplifiedChinese("zh_cn", setOf("CHN"));

    companion object {
        private var systemLanguage: GameLanguage? = null

        fun initialize() {
            systemLanguage = getSystem()
        }

        fun getMinecraft(): GameLanguage {
            val minecraftLanguage = Minecraft.getInstance().options.languageCode

            return values().firstOrNull { it.code == minecraftLanguage } ?: English
        }


        fun getSystem(): GameLanguage? {
            if (systemLanguage != null) return systemLanguage

            val systemCountry = Locale.getDefault().isO3Country
            return values().firstOrNull { it.isO3Code.contains(systemCountry) }
        }
    }
}