package com.rpmtw.rpmtw_platform_mod.translation.language

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import java.util.*

enum class TranslateLanguage(val code: String) {
    English("en_us"), TraditionalChinese("zh_tw"), SimplifiedChinese("zh_cn");

    companion object {
        fun getLanguage(): TranslateLanguage {
            return if (!RPMTWConfig.registered() || RPMTWConfig.get().translate.autoToggleLanguage) {
                val locale = Locale.getDefault()
                val countryCode = locale.country

                if (countryCode.contains("TW") || countryCode.contains("HK")) {
                    TraditionalChinese
                } else if (countryCode.contains("CN")) {
                    SimplifiedChinese
                } else {
                    English
                }
            } else {
                English
            }
        }
    }
}