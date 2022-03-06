package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TranslatableComponent

class ReplyCosmicMessageCommand {
    init {
        val argumentName = "message_uuid"
        RPMTWPlatformModPlugin.registerClientCommand("cosmicChat", "reply", argumentName, StringArgumentType.string()) {
            val uuid: String = StringArgumentType.getString(it, argumentName)
            Utilities.coroutineLaunch {
                val message: CosmicChatMessage? = CosmicChatHandler.getMessageAsync(uuid)
                val exception =
                    SimpleCommandExceptionType(TranslatableComponent("command.rpmtw_platform_mod.replyCosmicMessage.getMessage.failed")).create()
                if (message == null) {
                    throw exception
                } else {
                    Minecraft.getInstance().executeBlocking {
                        Minecraft.getInstance().setScreen(CosmicChatScreen(CosmicChatScreenType.Reply, message))
                    }
                }
            }
            return@registerClientCommand RPMTWCommand.success
        }
    }
}