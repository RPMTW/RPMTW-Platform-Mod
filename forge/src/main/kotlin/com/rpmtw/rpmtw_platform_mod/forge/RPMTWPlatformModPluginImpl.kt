package com.rpmtw.rpmtw_platform_mod.forge

import com.mojang.brigadier.CommandDispatcher
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.command.LoginRPMTWAccountCommand
import com.rpmtw.rpmtw_platform_mod.command.UniverseMessageActionCommand
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraftforge.fml.ExtensionPoint
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import java.io.File
import java.util.function.BiFunction

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        LOADING_CONTEXT.registerExtensionPoint(
            ExtensionPoint.CONFIGGUIFACTORY
        ) {
            BiFunction { _: Minecraft, parent: Screen? -> RPMTWConfig.getScreen(parent) }
        }
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>) -> Unit) {
        TODO("Forge not support client command")
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        RPMTWPlatformMod.LOGGER.info("Command: $command")
        if (command.startsWith("/rpmtw login")) {
            LoginRPMTWAccountCommand.execute()
        } else if (command.startsWith("/rpmtw universeChatAction")) {
            val uuid = command.substring("/rpmtw universeChatAction ".length)
            UniverseMessageActionCommand.execute(uuid)
        }

        return true
    }

    @JvmStatic
    fun getGameFolder(): File {
        return Minecraft.getInstance().gameDirectory
    }
}