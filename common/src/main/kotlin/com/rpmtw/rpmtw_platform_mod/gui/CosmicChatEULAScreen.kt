package com.rpmtw.rpmtw_platform_mod.gui

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent

class CosmicChatEULAScreen(private val initMessage: String?) : Screen(TextComponent("")) {
    override fun init() {
        addRenderableWidget(Button(
            width / 2 + 50,
            height / 2 + 30,
            BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
            TranslatableComponent("cosmicChat.rpmtw_platform_mod.gui.eula.disagree")
        ) {
            Minecraft.getInstance().setScreen(null)
        })
        addRenderableWidget(
            Button(
                (width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                height / 2 + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                TranslatableComponent("cosmicChat.rpmtw_platform_mod.gui.eula.agree"), Button.OnPress {
                    RPMTWConfig.get().cosmicChat.eula = true
                    RPMTWConfig.save()
                    if (initMessage != null && initMessage.isNotEmpty()) {
                        CosmicChatHandler.send(initMessage)
                        Minecraft.getInstance().setScreen(null)
                    } else {
                        Minecraft.getInstance().setScreen(CosmicChatScreen())
                    }
                })
        )

        addRenderableWidget(Button(
            (width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
            height / 2 + 30,
            BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
            TranslatableComponent("cosmicChat.rpmtw_platform_mod.gui.what")
        ) {
            Util.getPlatform().openUri("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
        })
    }

    override fun render(
        matrixStack: PoseStack,
        mouseX: Int, mouseY: Int, partialTicks: Float
    ) {
        this.renderBackground(matrixStack)
        val height = height / 2
        val textColor = 0xFFFFFF // White
        val title = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.title")
        val text1 = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.subtitle")
        val text2 = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.text.1")
        val text3 = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.text.2")
        val text4 = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.text.3")
        val text5 = I18n.get("cosmicChat.rpmtw_platform_mod.gui.eula.text.4")

        font.draw(matrixStack, title, width / 2f - font.width(title) / 2f, (height - 65).toFloat(), 0xFF5555)
        font.draw(matrixStack, text1, width / 2f - font.width(text1) / 2f, (height - 50).toFloat(), textColor)
        font.draw(matrixStack, text2, width / 2f - font.width(text2) / 2f, (height - 40).toFloat(), textColor)
        font.draw(matrixStack, text3, width / 2f - font.width(text2) / 2f, (height - 30).toFloat(), textColor)
        font.draw(matrixStack, text4, width / 2f - font.width(text2) / 2f, (height - 20).toFloat(), textColor)
        font.draw(matrixStack, text5, width / 2f - font.width(text2) / 2f, (height - 10).toFloat(), textColor)

        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    companion object {
        const val BUTTON_HEIGHT = 20
        private const val BOTTOM_BUTTON_WIDTH = 95
    }
}