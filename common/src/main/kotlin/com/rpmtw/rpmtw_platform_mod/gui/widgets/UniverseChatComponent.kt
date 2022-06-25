package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import net.minecraft.network.chat.TextComponent

class UniverseChatComponent(message: UniverseChatMessage) : TextComponent("") {
    private var message: UniverseChatMessage
    val universeChatMessage: UniverseChatMessage
        get() = this.message

    init {
        this.message = message
    }
}