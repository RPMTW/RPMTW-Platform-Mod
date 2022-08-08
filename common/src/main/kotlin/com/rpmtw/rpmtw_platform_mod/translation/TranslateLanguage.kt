package com.rpmtw.rpmtw_platform_mod.translation

import java.util.*

enum class TranslateLanguage(val code: String) {
    English("en_us"),
    TraditionalChinese("zh_tw"),
    SimplifiedChinese("zh_cn");

    companion object {
        fun getLanguage(): TranslateLanguage {
            val locale = Locale.getDefault()
            val countryCode = locale.country

            return if (countryCode.contains("TW") || countryCode.contains("HK")) {
                TraditionalChinese
            } else if (countryCode.contains("CN")) {
                SimplifiedChinese
            } else {
                English
            }
        }
    }
}