package com.rpmtw.rpmtw_platform_mod.gui

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.TranslucentButton
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

class UniverseMessageActionScreen(val message: UniverseChatMessage) :
    Screen(Component.empty()) {

    override fun init() {
        val replyButton = TranslucentButton(
            width / 2 - 50,
            height / 2 - GuiUtil.buttonHeight,
            GuiUtil.buttonWidth,
            GuiUtil.buttonHeight,
            Component.translatable("universeChat.rpmtw_platform_mod.gui.action.reply"), {
                Minecraft.getInstance().setScreen(UniverseChatScreen(UniverseChatScreenType.Reply, message))
            }, Component.translatable("universeChat.rpmtw_platform_mod.gui.action.reply.tooltip", message.username)
        )

        val blockUserButton = TranslucentButton(
            width / 2 - 50,
            replyButton.y + replyButton.height + 10,
            GuiUtil.buttonWidth,
            GuiUtil.buttonHeight,
            Component.translatable("universeChat.rpmtw_platform_mod.gui.action.blockUser").setStyle(
                Style.EMPTY.withColor(ChatFormatting.RED)
            ), {
                RPMTWConfig.get().universeChat.blockUsers.add(message.userIdentifier)
                GuiUtil.closeScreen()
            }, Component.translatable("universeChat.rpmtw_platform_mod.gui.action.blockUser.tooltip", message.username)
        )

        val closeScreenButton = TranslucentButton(
            width / 2 - 50,
            blockUserButton.y + blockUserButton.height + 10,
            GuiUtil.buttonWidth,
            GuiUtil.buttonHeight,
            Component.translatable("gui.rpmtw_platform_mod.close"), {
                GuiUtil.closeScreen()
            }, null
        )

        addRenderableWidget(replyButton)
        addRenderableWidget(blockUserButton)
        addRenderableWidget(closeScreenButton)

        super.init()
    }

    override fun render(
        poseStack: PoseStack,
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float
    ) {
        this.renderBackground(poseStack)

        val title = Component.translatable("universeChat.rpmtw_platform_mod.gui.action")
        font.draw(poseStack, title, width / 2f - font.width(title) / 2f, 20f, 0xFFFFFF)

        super.render(poseStack, mouseX, mouseY, partialTicks)
    }
}