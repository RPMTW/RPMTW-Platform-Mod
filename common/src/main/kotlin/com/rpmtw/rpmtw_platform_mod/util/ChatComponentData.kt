package com.rpmtw.rpmtw_platform_mod.util

import net.minecraft.resources.ResourceLocation

internal object ChatComponentData {
    const val offset = 10
    var lastY = 0
    var lastMessageIndex = 0
    var lastOpacity = 0f
    val avatarCache: Map<String, ResourceLocation> = HashMap()

    fun isAvatarCached(url: String): Boolean {
        return avatarCache.containsKey(url)
    }
}