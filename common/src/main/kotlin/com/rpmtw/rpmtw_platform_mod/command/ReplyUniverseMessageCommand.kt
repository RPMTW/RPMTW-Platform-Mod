package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TranslatableComponent

class ReplyUniverseMessageCommand {
    init {
        val argumentName = "message_uuid"
        RPMTWPlatformModPlugin.registerClientCommand("universeChat", "reply", argumentName, StringArgumentType.string()) {
            val uuid: String = StringArgumentType.getString(it, argumentName)
            Utilities.coroutineLaunch {
                val message: UniverseChatMessage? = UniverseChatHandler.getMessageAsync(uuid)
                val exception =
                    SimpleCommandExceptionType(TranslatableComponent("command.rpmtw_platform_mod.replyUniverseMessage.getMessage.failed")).create()
                if (message == null) {
                    throw exception
                } else {
                    Minecraft.getInstance().executeBlocking {
                        Minecraft.getInstance().setScreen(UniverseChatScreen(UniverseChatScreenType.Reply, message))
                    }
                }
            }
            return@registerClientCommand RPMTWCommand.success
        }
    }
}