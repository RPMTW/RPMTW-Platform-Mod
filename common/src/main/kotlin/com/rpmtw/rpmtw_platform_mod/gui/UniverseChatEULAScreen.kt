package com.rpmtw.rpmtw_platform_mod.gui

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.UniverseChatWhatButton
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent

class UniverseChatEULAScreen(private val initMessage: String?) : Screen(TextComponent.EMPTY) {
    override fun init() {

        val agreeButton = Button(
            (width - 100) / 2 - GuiUtil.buttonWidth,
            height / 2 + 30,
            GuiUtil.buttonWidth, GuiUtil.buttonHeight,
            TranslatableComponent("universeChat.rpmtw_platform_mod.gui.eula.agree")
        ) {
            RPMTWConfig.get().universeChat.eula = true
            RPMTWConfig.save()
            Util.openUniverseChatScreen(initMessage)
        }

        val disagreeButton = Button(
            (width - 4) / 2 - GuiUtil.buttonWidth + 50,
            height / 2 + 30,
            GuiUtil.buttonWidth, GuiUtil.buttonHeight,
            TranslatableComponent("universeChat.rpmtw_platform_mod.gui.eula.disagree")
        ) {
            GuiUtil.closeScreen()
        }

        val whatButton = UniverseChatWhatButton(width, height)

        addRenderableWidget(disagreeButton)
        addRenderableWidget(agreeButton)
        addRenderableWidget(whatButton)
    }

    override fun render(
        matrixStack: PoseStack,
        mouseX: Int, mouseY: Int, partialTicks: Float
    ) {
        this.renderBackground(matrixStack)
        val height = height / 2
        val textColor = 0xFFFFFF // White
        val title = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.title")
        val text1 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.subtitle")
        val text2 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.1")
        val text3 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.2")
        val text4 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.3")
        val text5 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.4")


        font.draw(matrixStack, title, width / 2f - font.width(title) / 2f, (height - 65).toFloat(), 0xFF5555)
        font.draw(matrixStack, text1, width / 2f - font.width(text1) / 2f, (height - 50).toFloat(), textColor)

        val fontWidth: Int = font.width(text2)

        font.draw(matrixStack, text2, width / 2f - fontWidth / 2f, (height - 40).toFloat(), textColor)
        font.draw(matrixStack, text3, width / 2f - fontWidth / 2f, (height - 30).toFloat(), textColor)
        font.draw(matrixStack, text4, width / 2f - fontWidth / 2f, (height - 20).toFloat(), textColor)
        font.draw(matrixStack, text5, width / 2f - fontWidth / 2f, (height - 10).toFloat(), textColor)

        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }
}