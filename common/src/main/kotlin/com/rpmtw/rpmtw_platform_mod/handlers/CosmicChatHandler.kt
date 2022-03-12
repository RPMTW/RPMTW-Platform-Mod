package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage
import com.rpmtw.rpmtw_api_client.resources.CosmicChatMessageFormat
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.CosmicChatAccountType
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.CosmicChatComponent
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import kotlinx.coroutines.Deferred
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.User
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.*

object CosmicChatHandler {
    private val client: RPMTWApiClient = RPMTWApiClient.instance
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
        if (RPMTWConfig.get().base.isLogin() && RPMTWConfig.get().cosmicChat.accountType == CosmicChatAccountType.RPMTW) {
            client.cosmicChatResource.connect(token = RPMTWConfig.get().base.rpmtwAuthToken!!)
        } else {
            client.cosmicChatResource.connect(minecraftUUID = user.uuid)
        }
    }

    private fun formatUrl(message: String): MutableComponent {
        val component: MutableComponent = TextComponent.EMPTY.copy()
        @Suppress("RegExpRedundantEscape") val urlRegex =
            Regex("(http|https):\\/\\/[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&amp;:/~+#-]*[\\w@?^=%&amp;/~+#-])?")
        val urlMatchList: List<MatchResult> = urlRegex.findAll(message).toList()
        var lastEnd = 0
        if (urlMatchList.isNotEmpty()) {
            for (match in urlMatchList) {
                component.append(message.substring(lastEnd, match.range.first))
                val url = match.value
                val urlComponent = TextComponent(url).setStyle(
                    Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.BLUE)
                        .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, url))
                )
                component.append(urlComponent)
                lastEnd = match.range.last
            }
            component.append(message.substring(lastEnd + 1))
        } else {
            component.append(TextComponent(message))
        }
        return component
    }

    private fun formatEmoji(message: String): String {
        var newMessage = message
        val emojiMap: Map<String, String> = mapOf(
            Pair(":big:", "\uF001"),
            Pair(":master_old:", "\uF002"),
            Pair(":can:", "\uF003"),
            Pair(":cannot:", "\uF004"),
            Pair(":eem:", "\uF005"),
            Pair(":Fabric:", "\uF006"),
            Pair(":Forge:", "\uF007"),
            Pair(":Forgeisgarbage:", "\uF008"),
            Pair(":LUL1:", "\uF009"),
            Pair(":LUL2:", "\uF010"),
            Pair(":not_know:", "\uF011"),
            Pair(":oao_light:", "\uF012"),
            Pair(":black_question:", "\uF014"),
            Pair(":rpmtw_team_logo:", "\uF015"),
            Pair(":RPMLauncher:", "\uF016"),
            Pair(":dangerous:", "\uF017"),
            Pair(":fear:", "\uF018"),
            Pair(":check_star:", "\uF019"),
            Pair(":oao_dark:", "\uF013"),
            Pair(":yellow_thinking:", "\uF020"),
            Pair(":SiongSng:", "\uF021"),
            Pair(":rpmwiki_logo:", "\uF022"),
            Pair(":rpmwiki_logo_complex:", "\uF023"),
        )
        emojiMap.forEach {
            newMessage = message.replace(it.key, it.value)
        }
        return newMessage
    }

    private fun formatAuthorName(message: CosmicChatMessage): String {
        return if (message.nickname != null && message.nickname!!.isNotEmpty()) "${message.username} (${message.nickname})" else message.username
    }

    private fun listenMessages() {
        if (client.cosmicChatResource.isConnected) {
            client.cosmicChatResource.onMessageSent({ msg ->
                if (RPMTWConfig.get().cosmicChat.enable && RPMTWConfig.get().cosmicChat.enableReceiveMessage) {
                    Utilities.coroutineLaunch {
                        val isReply: Boolean = msg.replyMessageUUID != null

                        val component: MutableComponent = TextComponent.EMPTY.copy()
                        val title = TextComponent("[${I18n.get("cosmicChat.rpmtw_platform_mod.title")}] ").setStyle(
                            Style.EMPTY.withColor(ChatFormatting.BLUE)
                        )
                        val authorName: String = formatAuthorName(msg)
                        val authorStyle: Style = Style.EMPTY.withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.OPEN_URL,
                                msg.avatarUrl
                            )
                        ).withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                TextComponent(I18n.get("cosmicChat.rpmtw_platform_mod.open_avatar_url"))
                            )
                        )
                        val author = TextComponent("§e<§6${authorName}§e> ").setStyle(authorStyle)


                        val replyAction: MutableComponent =
                            TextComponent("  [${I18n.get("gui.rpmtw_platform_mod.reply")}]").setStyle(
                                Style.EMPTY.withColor(ChatFormatting.GREEN).withClickEvent(
                                    ClickEvent(
                                        ClickEvent.Action.RUN_COMMAND,
                                        "/cosmicChat reply ${msg.uuid}"
                                    )
                                ).withHoverEvent(
                                    HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        TextComponent(I18n.get("cosmicChat.rpmtw_platform_mod.gui.reply", authorName))
                                    )
                                )
                            )

                        val messageContent: MutableComponent = formatUrl(formatEmoji(msg.message))
                        val message: MutableComponent = TextComponent.EMPTY.copy()
                        if (isReply) {
                            val replyMessage: CosmicChatMessage? = getMessageAsync(msg.replyMessageUUID!!)

                            if (replyMessage != null) {

                                val replyAuthorName: String = if (msg.avatarUrl == replyMessage.avatarUrl) {
                                    I18n.get("gui.rpmtw_platform_mod.self")
                                } else {
                                    formatAuthorName(replyMessage)
                                }
                                message.append(
                                    "§a${I18n.get("gui.rpmtw_platform_mod.reply")} §6${
                                        replyAuthorName
                                    } §b${replyMessage.message} §a-> §f${messageContent}"
                                )
                            }
                        } else {
                            message.append(messageContent)
                        }

                        component.append(title)
                        component.append(author)
                        component.append(message)
                        component.append(CosmicChatComponent(msg))
                        component.append(replyAction)
                        // Message format
                        // [Cosmic Chat] <Steve> Hello World!  [Reply]
                        // Reply message format
                        // [Cosmic Chat] <Steve> Reply Alex Hi! -> Hello World!  [Reply]
                        Minecraft.getInstance().player?.displayClientMessage(component, false)
                    }
                }
            }, format = CosmicChatMessageFormat.MinecraftFormatting)
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
        }

    }

    private fun statusHandler(status: String) {
        val prefix = "§9[${I18n.get("cosmicChat.rpmtw_platform_mod.title")}]§r"
        when (status) {
            "success" -> {
                Utilities.sendMessage(
                    "$prefix ${I18n.get("cosmicChat.rpmtw_platform_mod.status.success")}",
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

    private suspend fun sendMessage(message: String) {
        val status: String = client.cosmicChatResource.sendMessage(message, nickname = nickname)
        statusHandler(status)
    }

    private suspend fun replyMessage(message: String, uuid: String) {
        val status: String = client.cosmicChatResource.replyMessage(message = message, uuid = uuid, nickname = nickname)
        statusHandler(status)
    }

    fun handle() {
        if (RPMTWConfig.get().cosmicChat.enable) {
            RPMTWPlatformMod.LOGGER.info("Initializing cosmic chat server...")
            Utilities.coroutineLaunch {
                init()
                listen()
            }

            RPMTWPlatformMod.LOGGER.info("Cosmic chat initialized!")
        }
    }

    fun listen() {
        val result: Deferred<Unit> = Utilities.coroutineAsync {
            listenMessages()
        }
        Utilities.coroutineLaunch {
            result.await()
        }
    }

    fun send(message: String) {
        if (client.cosmicChatResource.isConnected) {
            Utilities.coroutineLaunch {
                sendMessage(message)
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
        }
    }

    fun reply(message: String, uuid: String) {
        if (client.cosmicChatResource.isConnected) {
            Utilities.coroutineLaunch {
                replyMessage(message, uuid)
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
        }
    }

    suspend fun getMessageAsync(uuid: String): CosmicChatMessage? {
        if (client.cosmicChatResource.isConnected) {
            val result: Deferred<CosmicChatMessage?> = Utilities.coroutineAsync {
                try {
                    return@coroutineAsync client.cosmicChatResource.getMessage(uuid)
                } catch (e: Exception) {
                    RPMTWPlatformMod.LOGGER.error("Failed to get message", e)
                    return@coroutineAsync null
                }
            }

            return result.await()
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to cosmic chat server failed")
            return null
        }
    }

    fun close() {
        client.cosmicChatResource.disconnect()
    }

    fun reset() {
        RPMTWPlatformMod.LOGGER.info("Resetting cosmic chat...")
        close()
        handle()
        RPMTWPlatformMod.LOGGER.info("Cosmic chat reset!")
    }
}