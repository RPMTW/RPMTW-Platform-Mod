package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.registerClientCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TranslatableComponent
import kotlin.coroutines.CoroutineContext

@Environment(EnvType.CLIENT)
object RPMTWCommand {
    private const val success: Int = Command.SINGLE_SUCCESS

    @ExperimentalCoroutinesApi
    private fun replyCosmicMessage() {
        val argumentName = "message_uuid"
        registerClientCommand("cosmicChat", "reply", argumentName, StringArgumentType.string()) {
            val uuid: String = StringArgumentType.getString(it, argumentName)
            val coroutineScope = object : CoroutineScope {
                override val coroutineContext: CoroutineContext
                    get() = Job()
            }
            coroutineScope.launch {

                RPMTWPlatformMod.LOGGER.info("test")
                val message: CosmicChatMessage? = CosmicChatHandler.getMessageAsync(uuid)
                val exception =
                    SimpleCommandExceptionType(TranslatableComponent("command.rpmtw_platform_mod.replyCosmicMessage.getMessage.failed")).create()
                if (message == null) {
                    throw exception
                } else {
                    Minecraft.getInstance().executeBlocking {
                        Minecraft.getInstance().setScreen(CosmicChatScreen(CosmicChatScreenType.Reply, message))
                    }
                }
            }
            return@registerClientCommand success
        }
    }

    @ExperimentalCoroutinesApi
    fun handle() {
        replyCosmicMessage()
    }
}