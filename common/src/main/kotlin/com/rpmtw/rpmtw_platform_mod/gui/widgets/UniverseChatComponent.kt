package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextComponent
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

    override fun getContents(): String {
        return ""
    }

    override fun getSiblings(): MutableList<Component> {
        return mutableListOf()
    }

    override fun plainCopy(): MutableComponent {
        return TextComponent.EMPTY.copy()
    }

    override fun copy(): MutableComponent {
        return TextComponent.EMPTY.copy()
    }


    override fun getVisualOrderText(): FormattedCharSequence {
        return FormattedCharSequence.EMPTY
    }
}