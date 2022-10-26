package com.rpmtw.rpmtw_platform_mod.gui

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.gui.widgets.UniverseChatWhatButton
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component

class UniverseChatScreen(
    private val type: UniverseChatScreenType,
    private val toReply: UniverseChatMessage? = null
) :
    Screen(Component.empty()) {
    private lateinit var messageEditBox: EditBox

    override fun init() {
        val whatButton = UniverseChatWhatButton(width, height)

        val sendButton = Button(
            (width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
            height / 2 + 30,
            BOTTOM_BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Component.translatable("gui.rpmtw_platform_mod.${type.name.lowercase()}")
        ) {
            val message: String = messageEditBox.value
            if (message.isEmpty()) {
                Util.sendMessage(I18n.get("universeChat.rpmtw_platform_mod.gui.input.null"), overlay = true)
            } else {
                Util.sendMessage(
                    "[${I18n.get("universeChat.rpmtw_platform_mod.title")}] ${I18n.get("universeChat.rpmtw_platform_mod.status.sending")}",
                    overlay = true
                )

                if (type.isSend) {
                    UniverseChatHandler.send(message)
                }

                if (type.isReply) {
                    if (toReply == null) {
                        throw IllegalStateException("toReply is null")
                    }
                    UniverseChatHandler.reply(message, toReply.uuid)
                }

            }
            Minecraft.getInstance().setScreen(null)
        }


        val cancelButton = Button(
            (width - 100) / 2 - BOTTOM_BUTTON_WIDTH, height / 2 + 30, BOTTOM_BUTTON_WIDTH,
            BUTTON_HEIGHT, Component.translatable("gui.rpmtw_platform_mod.cancel")
        ) {
            Minecraft.getInstance().setScreen(null)
        }

        val suggestion: String = I18n.get("universeChat.rpmtw_platform_mod.gui.input.tooltip")
        val messageEditBoxWidth = (font.width(suggestion) * 1.5).toInt()

        messageEditBox = object : EditBox(
            font,
            (width - messageEditBoxWidth) / 2,
            height / 2 - 10,
            messageEditBoxWidth,
            20,
            Component.literal(suggestion),
        ) {
            init {
                setSuggestion(suggestion)
            }

            override fun insertText(text: String) {
                super.insertText(text)
                if (value.isEmpty()) setSuggestion(suggestion) else setSuggestion(null)
            }

            override fun moveCursorTo(pos: Int) {
                super.moveCursorTo(pos)
                if (value.isEmpty()) setSuggestion(suggestion) else setSuggestion(null)
            }
        }
        messageEditBox.setMaxLength(150)

        addRenderableWidget(whatButton)
        addRenderableWidget(sendButton)
        addRenderableWidget(cancelButton)
        addWidget(messageEditBox)
    }

    override fun render(
        matrixStack: PoseStack,
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float
    ) {
        this.renderBackground(matrixStack)
        val height = height / 2
        lateinit var title: String

        if (type.isSend) {
            title = I18n.get("universeChat.rpmtw_platform_mod.gui.send")
        } else if (type.isReply) {
            if (toReply == null) {
                throw IllegalStateException("toReply is null")
            }

            title = I18n.get("universeChat.rpmtw_platform_mod.gui.reply", toReply.username)
        }

        font.draw(
            matrixStack, title, width / 2f - font.width(title) / 2f, (height - 35).toFloat(),
            0xFF5555
        )
        messageEditBox.render(matrixStack, mouseX, mouseY, partialTicks)
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    companion object {
        const val BUTTON_HEIGHT = 20
        private const val BOTTOM_BUTTON_WIDTH = 95
    }
}

enum class UniverseChatScreenType {
    Send,
    Reply;

    val isSend: Boolean
        get() = this == Send

    val isReply: Boolean
        get() = this == Reply
}