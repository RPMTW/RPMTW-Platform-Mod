package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.SharedSuggestionProvider
import org.quiltmc.loader.api.QuiltLoader
import org.quiltmc.qsl.command.api.client.ClientCommandRegistrationCallback
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource
import org.quiltmc.qsl.command.impl.client.ClientCommandInternals
import java.io.File

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (CommandDispatcher<SharedSuggestionProvider>, buildContext: CommandBuildContext) -> Unit) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<QuiltClientCommandSource?>, buildContext: CommandBuildContext, _ ->
            @Suppress("UNCHECKED_CAST")
            callback(dispatcher as CommandDispatcher<SharedSuggestionProvider>, buildContext)
        })
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        return ClientCommandInternals.executeCommand(command, false)
    }

    @JvmStatic
    fun getGameFolder(): File {
        return QuiltLoader.getGameDir().toFile()
    }
}