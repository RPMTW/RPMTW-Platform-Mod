package com.rpmtw.rpmtw_platform_mod.data

import net.minecraft.resources.ResourceLocation

object ChatComponentData {
    const val offset = 10
    var lastY = 0
    var lastMessageIndex = 0
    var lastOpacity = 0f
    var cosmicChatAvatarCache: MutableMap<String, ResourceLocation?> = HashMap()
}