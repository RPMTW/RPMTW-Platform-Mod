package com.rpmtw.rpmtw_platform_mod.util

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatEULAScreen
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreen
import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreenType
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.io.File
import kotlin.coroutines.CoroutineContext
import net.minecraft.Util as MCUtil


object Util {
    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job()
    }

    fun sendMessage(message: String, overlay: Boolean = false) {
        Minecraft.getInstance().player?.displayClientMessage(Component.literal(message), overlay)
    }

    fun openUniverseChatScreen(initMessage: String?) {
        if (RPMTWConfig.get().universeChat.eula) {
            if (!initMessage.isNullOrEmpty() && !initMessage.startsWith("/")) {
                UniverseChatHandler.send(initMessage)
                Minecraft.getInstance().setScreen(null)
            } else {
                Minecraft.getInstance().setScreen(UniverseChatScreen(UniverseChatScreenType.Send))
            }
        } else {
            Minecraft.getInstance().setScreen(UniverseChatEULAScreen(initMessage))
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

    private fun getBaseDirectory(): File {
        return File(System.getProperty("java.io.tmpdir")).resolve("com.rpmtw.rpmtw_platform_mod")
    }

    fun getFileLocation(fileName: String): File {
        val baseDirectory: File = getBaseDirectory()
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs()
        }

        return baseDirectory.resolve(fileName)
    }

    fun getUniverseChatAvatarLocation(hash: String): File {
        val directory = getBaseDirectory().resolve("universe_chat_avatars")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        return directory.resolve(hash)
    }

    fun openLink(link: String) {
        MCUtil.getPlatform().openUri(link)
    }
}