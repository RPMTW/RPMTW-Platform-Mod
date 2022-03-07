package com.rpmtw.rpmtw_platform_mod.utilities

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatEULAScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TextComponent
import java.io.File
import kotlin.coroutines.CoroutineContext


object Utilities {
    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job()
    }

    fun sendMessage(message: String, overlay: Boolean = false) {
        Minecraft.getInstance().player?.displayClientMessage(TextComponent(message), overlay)
    }

    fun openCosmicChatScreen(initMessage: String?) {
        if (RPMTWConfig.get().cosmicChat.eula) {
            if (initMessage != null && initMessage.isNotEmpty() && !initMessage.startsWith("/")) {
                CosmicChatHandler.send(initMessage)
                Minecraft.getInstance().setScreen(null)
            } else {
                Minecraft.getInstance().setScreen(CosmicChatScreen(CosmicChatScreenType.Send))
            }
        } else {
            Minecraft.getInstance().setScreen(CosmicChatEULAScreen(initMessage))
        }
    }

    fun coroutineLaunch(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch {
            block()
        }
    }

    fun <T> coroutineAsync(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return coroutineScope.async {
            block()
        }
    }

    fun getBaseDirectory(): File {
        return File(System.getProperty("user.home")).resolve(".rpmtw")
    }

    fun getFileLocation(fileName: String): File {
        return getBaseDirectory().resolve(fileName)
    }
}