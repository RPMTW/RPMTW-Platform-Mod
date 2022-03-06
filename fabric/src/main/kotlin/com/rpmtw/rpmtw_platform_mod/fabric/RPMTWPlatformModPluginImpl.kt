package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String? = null,
        argumentType: ArgumentType<*>? = null,
        executes: (CommandContext<*>) -> Int
    ) {
        val dispatcher: CommandDispatcher<FabricClientCommandSource> = ClientCommandManager.DISPATCHER

        if (argumentName != null && argumentType != null) {
            dispatcher.register(
                literal(command).then(
                    literal(subCommand).then(argument(argumentName, argumentType).executes {
                        return@executes executes(it)
                    })
                )
            )
        } else {
            dispatcher.register(literal(command).then(literal(subCommand).executes {
                return@executes executes(it)
            }))
        }
    }
}