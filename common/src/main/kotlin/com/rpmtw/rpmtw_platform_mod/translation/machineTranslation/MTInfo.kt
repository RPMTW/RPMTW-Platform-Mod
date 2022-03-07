package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import java.sql.Timestamp

data class MTInfo(
    val text: String? = null,
    val timestamp: Timestamp,
    val error: Exception? = null,
    val status: MTDataStatus
)

enum class MTDataStatus {
    SUCCESS,
    Translating,
    FAILED
}