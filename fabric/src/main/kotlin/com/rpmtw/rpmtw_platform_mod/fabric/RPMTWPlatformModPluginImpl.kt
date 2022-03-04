package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun registerClientCommand(command: String, executes: () -> Int) {
        ClientCommandManager.DISPATCHER.register(literal(command).executes {
            return@executes executes()
        })
    }

    @JvmStatic
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String,
        argumentType: ArgumentType<*>,
        executes: (CommandContext<*>) -> Int
    ) {
        ClientCommandManager.DISPATCHER.register(
            literal(command).then(literal(subCommand).then(argument(argumentName, argumentType).executes {
                return@executes executes(it)
            }))
        )
    }
}