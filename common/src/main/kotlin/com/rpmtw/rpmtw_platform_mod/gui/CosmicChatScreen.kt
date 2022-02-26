package com.rpmtw.rpmtw_platform_mod.gui

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent

class CosmicChatScreen : Screen(TextComponent("")) {
    private var messageEditBox: EditBox? = null

    override fun init() {
        val whatButton = Button(
            width / 2 + 50, height / 2 + 30, BOTTOM_BUTTON_WIDTH,
            BUTTON_HEIGHT, TranslatableComponent("cosmicChat.rpmtw_platform_mod.gui.what")
        ) {
            Util.getPlatform().openUri("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
        }

        val sendButton = Button(
            (width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50, height / 2 + 30,
            BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT, TranslatableComponent("gui.rpmtw_platform_mod.send")
        ) {
            if (messageEditBox == null) return@Button
            if (messageEditBox!!.value.isEmpty()) {
                Utilities.sendMessage(I18n.get("cosmicChat.rpmtw_platform_mod.gui.input.null"))
            } else {
                CosmicChatHandler.send(messageEditBox!!.value)
            }
            Minecraft.getInstance().setScreen(null)
        }


        val cancelButton = Button(
            (width - 100) / 2 - BOTTOM_BUTTON_WIDTH, height / 2 + 30, BOTTOM_BUTTON_WIDTH,
            BUTTON_HEIGHT, TranslatableComponent("gui.rpmtw_platform_mod.cancel")
        ) {
            Minecraft.getInstance().setScreen(null)
        }

        val suggestion: String = I18n.get("cosmicChat.rpmtw_platform_mod.gui.input.tooltip")
        messageEditBox = object : EditBox(
            font, width / 2 - 95, height / 2 - 10, 200, 20,
            TextComponent(suggestion)
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
        messageEditBox!!.setMaxLength(150)

        addRenderableWidget(whatButton)
        addRenderableWidget(sendButton)
        addRenderableWidget(cancelButton)
        addWidget(messageEditBox!!)
    }

    override fun render(
        matrixStack: PoseStack,
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float
    ) {
        this.renderBackground(matrixStack)
        val height = height / 2
        val title = I18n.get("cosmicChat.rpmtw_platform_mod.gui.send")
        font.draw(
            matrixStack, title, width / 2f - font.width(title) / 2f, (height - 35).toFloat(),
            0xFF5555
        )
        messageEditBox!!.render(matrixStack, mouseX, mouseY, partialTicks)
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    companion object {
        const val BUTTON_HEIGHT = 20
        private const val BOTTOM_BUTTON_WIDTH = 95
    }
}