package com.rpmtw.rpmtw_platform_mod.util

import net.minecraft.resources.ResourceLocation

internal object ChatComponentData {
    var offset = 10
    var lastY = 0
    var lastMessageIndex = 0
    var lastOpacity = 0f
    var universeChatAvatarCache: Map<String, ResourceLocation> = HashMap()
}