package com.rpmtw.rpmtw_platform_mod.gui

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.UniverseChatWhatButton
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component

class UniverseChatEULAScreen(private val initMessage: String?) : Screen(Component.empty()) {
    override fun init() {

        val agreeButton = Button.builder(
            Component.translatable("universeChat.rpmtw_platform_mod.gui.eula.agree")
        ) {
            RPMTWConfig.get().universeChat.eula = true
            RPMTWConfig.save()
            Util.openUniverseChatScreen(initMessage)
        }.bounds(
            (width - 100) / 2 - GuiUtil.buttonWidth,
            height / 2 + 30,
            GuiUtil.buttonWidth, GuiUtil.buttonHeight
        ).build()

        val disagreeButton = Button.builder(

            Component.translatable("universeChat.rpmtw_platform_mod.gui.eula.disagree")
        ) {
            GuiUtil.closeScreen()
        }
            .bounds(
                (width - 4) / 2 - GuiUtil.buttonWidth + 50,
                height / 2 + 30,
                GuiUtil.buttonWidth,
                GuiUtil.buttonHeight
            )
            .build()

        val whatButton = UniverseChatWhatButton(width, height)

        addRenderableWidget(disagreeButton)
        addRenderableWidget(agreeButton)
        addRenderableWidget(whatButton)
    }

    override fun render(
        guiGraphices: GuiGraphics,
        mouseX: Int, mouseY: Int, partialTicks: Float
    ) {
        this.renderBackground(guiGraphices)
        val height = height / 2
        val textColor = 0xFFFFFF // White
        val title = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.title")
        val text1 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.subtitle")
        val text2 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.1")
        val text3 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.2")
        val text4 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.3")
        val text5 = I18n.get("universeChat.rpmtw_platform_mod.gui.eula.text.4")


        guiGraphices.drawString(font, title, width / 2 - font.width(title) / 2, height - 65, 0xFF5555)
        guiGraphices.drawString(font, text1, width / 2 - font.width(text1) / 2, height - 50, textColor)

        val fontWidth: Int = font.width(text2)

        guiGraphices.drawString(font, text2, width / 2 - fontWidth / 2, height - 40, textColor)
        guiGraphices.drawString(font, text3, width / 2 - fontWidth / 2, height - 30, textColor)
        guiGraphices.drawString(font, text4, width / 2 - fontWidth / 2, height - 20, textColor)
        guiGraphices.drawString(font, text5, width / 2 - fontWidth / 2, height - 10, textColor)

        super.render(guiGraphices, mouseX, mouseY, partialTicks)
    }
}