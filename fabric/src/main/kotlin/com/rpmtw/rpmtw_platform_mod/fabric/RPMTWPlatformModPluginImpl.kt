package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.SharedSuggestionProvider


@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>, buildContext: CommandBuildContext) -> Unit) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, buildContext: CommandBuildContext ->
            @Suppress("UNCHECKED_CAST")
            callback(dispatcher as CommandDispatcher<SharedSuggestionProvider>, buildContext)
        })
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        return ClientCommandInternals.executeCommand(command)
    }
}