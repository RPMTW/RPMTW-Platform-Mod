package com.rpmtw.rpmtw_platform_mod.forge

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig.getScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.client.event.RegisterClientCommandsEvent
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.io.File

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        LOADING_CONTEXT.registerExtensionPoint(ConfigScreenFactory::class.java) {
            ConfigScreenFactory { _: Minecraft?, screen: Screen? -> getScreen(screen) }
        }
    }

    @JvmStatic
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String? = null,
        argumentType: ArgumentType<*>? = null,
        executes: (CommandContext<*>) -> Int
    ) {
        FORGE_BUS.addListener { event: RegisterClientCommandsEvent ->
            if (argumentName != null && argumentType != null) {
                event.dispatcher.register(
                    literal(command).then(
                        literal(subCommand).then(argument(argumentName, argumentType).executes { executes(it) })
                    )
                )
            } else {
                event.dispatcher.register(
                    literal(command).then(literal(subCommand).executes { executes(it) })
                )
            }
        }
    }


    @JvmStatic
    fun <T> registerReloadEvent(reloadListener: SimplePreparableReloadListener<T>) {
        MOD_BUS.addListener { event: RegisterClientReloadListenersEvent ->
            event.registerReloadListener(reloadListener)
        }
    }

    @JvmStatic
    fun getGameFolder(): File {
        return Minecraft.getInstance().gameDirectory
    }
}