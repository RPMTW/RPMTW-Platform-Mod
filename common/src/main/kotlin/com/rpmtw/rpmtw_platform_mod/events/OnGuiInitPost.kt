package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.gui.widgets.RPMTWCheckbox
import com.rpmtw.rpmtw_platform_mod.gui.widgets.TranslucentButton
import com.rpmtw.rpmtw_platform_mod.mixins.ChatScreenAccessor
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import dev.architectury.hooks.client.screen.ScreenAccess
import dev.architectury.platform.Platform
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.TranslatableComponent

object OnGuiInitPost {
    fun handle(screen: Screen, screenAccess: ScreenAccess) {
        val scaledWidth = screen.width
        val scaledHeight = screen.height

        if (screen is ChatScreen && (RPMTWConfig.get().cosmicChat.enable && RPMTWConfig.get().cosmicChat.enableButton)) {
            val textField: EditBox? = (screen as ChatScreenAccessor.chatFieldAccessor).chatField
            val offsetX: Int
            val hasQuarkMod = Platform.isModLoaded("quark")
            offsetX = if (hasQuarkMod) {
                // Because quark mod has a button on the chat screen, we need to offset the button by the width of the quark button
                // https://github.com/VazkiiMods/Quark/blob/9c3334244d508d0b0383e7f397b02c136295067e/src/main/java/vazkii/quark/content/tweaks/module/EmotesModule.java#L224
                -75
            } else {
                0
            }

            val sendButton = TranslucentButton(scaledWidth - 185 + offsetX,
                scaledHeight - 40,
                90,
                20,
                TranslatableComponent("cosmicChat.rpmtw_platform_mod.button.send"),
                {
                    Utilities.openCosmicChatScreen(textField?.value)
                },
                { _, matrixStack, i, j ->
                    screen.renderTooltip(
                        matrixStack, TranslatableComponent("cosmicChat.rpmtw_platform_mod.button.send.tooltip"), i, j
                    )
                })

            val checkbox = RPMTWCheckbox(
                scaledWidth - 90 + offsetX,
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