package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.gui.UniverseMessageActionScreen
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.Minecraft
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component

class UniverseMessageActionCommand : RPMTWCommand() {
    override fun register(): LiteralArgumentBuilder<SharedSuggestionProvider> {
        val argumentName = "message_uuid"

        val command = literal("universeChatAction").then(argument(argumentName, StringArgumentType.string()).executes {
            val uuid: String = StringArgumentType.getString(it, argumentName)

            Util.coroutineLaunch {
                val message: UniverseChatMessage? = UniverseChatHandler.getMessageAsync(uuid)
                val exception =
                    SimpleCommandExceptionType(Component.translatable("command.rpmtw_platform_mod.universeChatAction.getMessage.failed")).create()
                if (message == null) {
                    throw exception
                } else {
                    Minecraft.getInstance().executeBlocking {
                        Minecraft.getInstance().setScreen(UniverseMessageActionScreen(message))
                    }
                }
            }
            return@executes CommandHandler.success
        })

        return command
    }
}