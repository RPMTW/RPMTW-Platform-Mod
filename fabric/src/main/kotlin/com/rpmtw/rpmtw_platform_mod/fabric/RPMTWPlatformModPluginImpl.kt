package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals
import net.minecraft.commands.SharedSuggestionProvider


@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        callback(
            ClientCommandManager.DISPATCHER as CommandDispatcher<SharedSuggestionProvider>,
        )
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        return ClientCommandInternals.executeCommand(command)
    }
}