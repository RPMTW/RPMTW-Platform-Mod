package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage
import net.minecraft.network.chat.TextComponent

class CosmicChatComponent(message: CosmicChatMessage) : TextComponent("") {
    private var message: CosmicChatMessage
    val cosmicChatMessage: CosmicChatMessage
        get() = this.message

    init {
        this.message = message
    }
}