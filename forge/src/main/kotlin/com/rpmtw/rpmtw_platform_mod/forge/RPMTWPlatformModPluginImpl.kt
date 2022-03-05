package com.rpmtw.rpmtw_platform_mod.forge

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig.getScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        LOADING_CONTEXT.registerExtensionPoint(ConfigGuiFactory::class.java) {
            ConfigGuiFactory { _: Minecraft?, screen: Screen? -> getScreen(screen) }
        }
    }

    @JvmStatic
    fun registerClientCommand(command: String, executes: () -> Int) {
        val dispatcher: CommandDispatcher<CommandSourceStack>? = ClientCommandHandler.getDispatcher()

        if (dispatcher == null) {
            RPMTWPlatformMod.LOGGER.error("Failed to register client command, because dispatcher is null")
        }

        dispatcher?.register(literal(command).executes {
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
        val dispatcher: CommandDispatcher<CommandSourceStack>? = ClientCommandHandler.getDispatcher()

        if (dispatcher == null) {
            RPMTWPlatformMod.LOGGER.error("Failed to register client command, because dispatcher is null")
        }

        dispatcher?.register(
            literal(command).then(literal(subCommand).then(argument(argumentName, argumentType).executes {
                return@executes executes(it)
            }))
        )
    }
}