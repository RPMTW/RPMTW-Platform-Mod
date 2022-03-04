package com.rpmtw.rpmtw_platform_mod.utilities

import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatEULAScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TextComponent

object Utilities {
    fun sendMessage(message: String, overlay: Boolean = false) {
        Minecraft.getInstance().player?.displayClientMessage(TextComponent(message), overlay)
    }

    fun openCosmicChatScreen(initMessage: String?) {
        if (RPMTWConfig.get().cosmicChat.eula) {
            if (initMessage != null && initMessage.isNotEmpty() && !initMessage.startsWith("/")) {
                CosmicChatHandler.send(initMessage)
                Minecraft.getInstance().setScreen(null)
            } else {
                Minecraft.getInstance().setScreen(CosmicChatScreen(CosmicChatScreenType.Send))
            }
        } else {
            Minecraft.getInstance().setScreen(CosmicChatEULAScreen(initMessage))
        }
    }
}