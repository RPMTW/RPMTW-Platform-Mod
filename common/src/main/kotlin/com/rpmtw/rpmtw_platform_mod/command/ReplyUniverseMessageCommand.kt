package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component

class ReplyUniverseMessageCommand : RPMTWCommand() {
    override fun register(): LiteralArgumentBuilder<SharedSuggestionProvider> {
        val argumentName = "message_uuid"

        val command = literal("universeChatReply").then(argument(argumentName, StringArgumentType.string()).executes {
            val uuid: String = StringArgumentType.getString(it, argumentName)

            Util.coroutineLaunch {
                val message: UniverseChatMessage? = UniverseChatHandler.getMessageAsync(uuid)
                val exception =
                    SimpleCommandExceptionType(Component.translatable("command.rpmtw_platform_mod.replyUniverseMessage.getMessage.failed")).create()
                if (message == null) {
                    throw exception
                } else {
                    Minecraft.getInstance().executeBlocking {
                        Minecraft.getInstance().setScreen(UniverseChatScreen(UniverseChatScreenType.Reply, message))
                    }
                }
            }
            return@executes CommandHandler.success
        })

        return command
    }
}