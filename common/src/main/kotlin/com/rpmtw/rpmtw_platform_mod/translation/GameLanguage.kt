package com.rpmtw.rpmtw_platform_mod.translation

import net.minecraft.client.Minecraft
import java.util.*

enum class GameLanguage(val code: String, val isO3Code: Set<String>) {
    English("en_us", setOf("ENG")),
    TraditionalChinese("zh_tw", setOf("TWN", "HKG")),
    SimplifiedChinese("zh_cn", setOf("CHN"));

    companion object {
        fun getMinecraft(): GameLanguage {
            val minecraftLanguage = Minecraft.getInstance().options.languageCode

            return values().firstOrNull { it.code == minecraftLanguage } ?: English
        }

        fun getSystem(): GameLanguage? {
            val systemLanguage = Locale.getDefault().isO3Language

            return values().firstOrNull { it.isO3Code.contains(systemLanguage) }
        }
    }
}