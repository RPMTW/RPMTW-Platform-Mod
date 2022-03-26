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
import java.util.*
import kotlin.coroutines.CoroutineContext


object Utilities {
    val languageCode: String
        get() {
            val langCode = Locale.getDefault().language
            val mcLangCode = Minecraft.getInstance().languageManager.selected.code

            return if (RPMTWConfig.get().translate.autoToggleLanguage && (langCode.contains("zh") || langCode.contains("chi"))) {
                val countryCode = Locale.getDefault().country
                if (countryCode.contains("TW") || countryCode.contains("HK")) {
                    "zh_tw"
                } else if (countryCode.contains("CN")) {
                    "zh_cn"
                } else {
                    mcLangCode
                }
            } else {
                mcLangCode
            }
        }

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
}