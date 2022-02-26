package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.gui.widgets.RPMTWCheckbox
import com.rpmtw.rpmtw_platform_mod.gui.widgets.TranslucentButton
import com.rpmtw.rpmtw_platform_mod.mixins.ChatScreenAccessor
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import dev.architectury.hooks.client.screen.ScreenAccess
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.TranslatableComponent

object OnGuiInitPost {
    fun handle(screen: Screen, screenAccess: ScreenAccess) {
        val scaledWidth = screen.width
        val scaledHeight = screen.height

        if (screen is ChatScreen && RPMTWConfig.get().cosmicChat.enableButton) {
              val textField: EditBox? = (screen as ChatScreenAccessor.chatFieldAccessor).chatField

            val sendButton = TranslucentButton(
                scaledWidth - 185,
                scaledHeight - 40,
                90,
                20,
                TranslatableComponent("cosmicChat.rpmtw_platform_mod.button.send"),
                {
                    TODO("Open cosmic chat screen")
                },
                { _, matrixStack, i, j ->
                    screen.renderTooltip(
                        matrixStack,
                        TranslatableComponent("cosmicChat.rpmtw_platform_mod.button.send.tooltip"),
                        i,
                        j
                    )
                }
            )

            val checkbox = RPMTWCheckbox(
                scaledWidth - 90,
                scaledHeight - 40,
                20,
                20,
                TranslatableComponent("cosmicChat.rpmtw_platform_mod.button.receive"),
                RPMTWConfig.get().cosmicChat.enableReceiveMessage,
                { checked -> RPMTWConfig.get().cosmicChat.enableReceiveMessage = checked },
                I18n.get("cosmicChat.rpmtw_platform_mod.button.receive.tooltip")
            )

            screenAccess.addRenderableWidget(sendButton)
            screenAccess.addRenderableWidget(checkbox)
        }
    }
}