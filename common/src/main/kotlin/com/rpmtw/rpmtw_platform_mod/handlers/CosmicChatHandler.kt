package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import kotlinx.coroutines.*
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.User
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextComponent
import kotlin.coroutines.CoroutineContext

object CosmicChatHandler {
    private val client: RPMTWApiClient = RPMTWApiClient.instance
    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job()
    }
    private val nickname: String?
        get() {
            return if (RPMTWConfig.get().cosmicChat.nickname != null && RPMTWConfig.get().cosmicChat.nickname!!.isNotEmpty()) {
                RPMTWConfig.get().cosmicChat.nickname!!
            } else {
                null
            }
        }

    private suspend fun init() {
        val minecraft: Minecraft = Minecraft.getInstance()
        val user: User = minecraft.user
        client.cosmicChatResource.connect(minecraftUUID = user.uuid)
    }

    private fun listenMessages() {
        if (client.cosmicChatResource.isConnected) {
            val minecraft: Minecraft = Minecraft.getInstance()
            val player: LocalPlayer? = minecraft.player

            client.cosmicChatResource.onMessageSent {
                if (player == null) {
                    RPMTWPlatformMod.LOGGER.error("player is null")
                    return@onMessageSent
                }
                if (!RPMTWConfig.get().cosmicChat.enable || !RPMTWConfig.get().cosmicChat.enableReceiveMessage) return@onMessageSent
                val component: MutableComponent = TextComponent.EMPTY.copy()
                val title = TextComponent("[${I18n.get("cosmicChat.rpmtw_platform_mod.title")}] ").setStyle(
                    Style.EMPTY.withColor(ChatFormatting.BLUE)
                )
                val authorName: String =
                    if (it.nickname != null && it.nickname!!.isNotEmpty()) "${it.username} (${it.nickname})" else it.username
                val author = TextComponent("§e<§6${authorName}§e> ")

                val messageContent = it.message
                val message: MutableComponent = TextComponent.EMPTY.copy()

                @Suppress("RegExpRedundantEscape") val urlRegex =
                    Regex("(http|https):\\/\\/[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&amp;:/~+#-]*[\\w@?^=%&amp;/~+#-])?")
                val urlMatchList: List<MatchResult> = urlRegex.findAll(messageContent).toList()
                var lastEnd = 0
                if (urlMatchList.isNotEmpty()) {
                    for (match in urlMatchList) {
                        message.append(messageContent.substring(lastEnd, match.range.first))
                        val url = match.value
                        val urlComponent = TextComponent(url).setStyle(
                            Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.BLUE)
                                .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, url))
                        )
                        message.append(urlComponent)
                        lastEnd = match.range.last
                    }
                    message.append(messageContent.substring(lastEnd))
                } else {
                    message.append(TextComponent(messageContent))
                }

                component.append(title)
                component.append(author)
                component.append(message)
                player.displayClientMessage(component, false)
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
        }
    }

    private suspend fun sendMessage(message: String) {
        val status: String = client.cosmicChatResource.sendMessage(message, nickname = nickname)
        val prefix = "§9[${I18n.get("cosmicChat.rpmtw_platform_mod.title")}]§r"

        when (status) {
            "success" -> {
                Utilities.sendMessage(
                    "$prefix ${I18n.get("cosmicChat.rpmtw_platform_mod.status.success")}！",
                    overlay = true
                )
            }
            "phishing" -> {
                Utilities.sendMessage(
                    "$prefix ${I18n.get("cosmicChat.rpmtw_platform_mod.status.phishing")}",
                    overlay = true
                )
            }
            "banned" -> {
                Utilities.sendMessage(
                    "$prefix ${I18n.get("cosmicChat.rpmtw_platform_mod.status.banned")}",
                    overlay = true
                )
            }
            "unauthorized" -> {
                Utilities.sendMessage(
                    "$prefix ${I18n.get("cosmicChat.rpmtw_platform_mod.status.unauthorized")}",
                    overlay = true
                )
            }
        }
    }

    fun handle() {
        if (RPMTWConfig.get().cosmicChat.enable) {
            RPMTWPlatformMod.LOGGER.info("Initializing cosmic chat server...")
            val result: Deferred<Unit> = coroutineScope.async {
                init()
            }
            coroutineScope.launch {
                result.await()
            }

            RPMTWPlatformMod.LOGGER.info("Cosmic chat initialized!")
        }
    }

    fun listen() {
        val result: Deferred<Unit> = coroutineScope.async {
            listenMessages()
        }
        coroutineScope.launch {
            result.await()
        }
    }

    fun send(message: String) {
        if (client.cosmicChatResource.isConnected) {
            val result: Deferred<Unit> = coroutineScope.async {
                sendMessage(message)
            }
            coroutineScope.launch {
                result.await()
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
        }
    }

    fun close() {
        client.cosmicChatResource.disconnect()
    }
}