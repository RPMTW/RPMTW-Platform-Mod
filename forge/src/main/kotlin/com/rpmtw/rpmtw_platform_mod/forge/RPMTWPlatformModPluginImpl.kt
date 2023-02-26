package com.rpmtw.rpmtw_platform_mod.forge

import com.mojang.brigadier.CommandDispatcher
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory
import net.minecraftforge.client.event.RegisterClientCommandsEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.io.File

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        LOADING_CONTEXT.registerExtensionPoint(
            ConfigGuiFactory::class.java
        ) {
            ConfigGuiFactory { _: Minecraft?, parent: Screen? ->
                RPMTWConfig.getScreen(parent)
            }
        }
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>) -> Unit) {
        FORGE_BUS.addListener { event: RegisterClientCommandsEvent ->
            @Suppress("UNCHECKED_CAST")
            callback(event.dispatcher as CommandDispatcher<SharedSuggestionProvider>)
        }
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        return ClientCommandHandler.sendMessage(command)
    }

    @JvmStatic
    fun getGameFolder(): File {
        return Minecraft.getInstance().gameDirectory
    }

    @JvmStatic
    fun getGameFolder(): File {
        return Minecraft.getInstance().gameDirectory
    }
}