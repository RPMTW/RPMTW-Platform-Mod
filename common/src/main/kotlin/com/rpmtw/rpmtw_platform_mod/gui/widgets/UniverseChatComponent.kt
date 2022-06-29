package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

class UniverseChatComponent(message: UniverseChatMessage) : Component {
    private var message: UniverseChatMessage
    val universeChatMessage: UniverseChatMessage
        get() = this.message

    init {
        this.message = message
    }

    override fun getStyle(): Style {
        return Style.EMPTY
    }

    override fun getContents(): ComponentContents {
        return ComponentContents.EMPTY
    }

    override fun getSiblings(): MutableList<Component> {
        return mutableListOf()
    }

    override fun getVisualOrderText(): FormattedCharSequence {
        return FormattedCharSequence.EMPTY
    }
}