package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.config.UniverseChatAccountType
import com.rpmtw.rpmtw_platform_mod.util.Util
import kotlinx.coroutines.Deferred
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.User
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.*
import com.rpmtw.rpmtw_api_client.util.Util as ApiClientUtil

object UniverseChatHandler {
    private val client: RPMTWApiClient = RPMTWApiClient.instance
    private val nickname: String?
        get() {
            return if (RPMTWConfig.get().universeChat.nickname != null && RPMTWConfig.get().universeChat.nickname!!.isNotEmpty()) {
                RPMTWConfig.get().universeChat.nickname!!
            } else {
                null
            }
        }

    private suspend fun init() {
        if (RPMTWConfig.get().isLogin() && RPMTWConfig.get().universeChat.accountType == UniverseChatAccountType.RPMTW) {
            this.client.universeChatResource.connect(token = RPMTWConfig.get().rpmtwAuthToken!!)
        } else {
            val user: User = Minecraft.getInstance().user
            this.client.universeChatResource.connect(minecraftUUID = user.uuid)
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
        var result = message

        val emojiMap: Map<String, String> = mapOf(
            Pair(":big:", "\uF001"),
            Pair(":master_old:", "\uF002"),
            Pair(":can:", "\uF003"),
            Pair(":cannot:", "\uF004"),
            Pair(":eem:", "\uF005"),
            Pair(":fabric:", "\uF006"),
            Pair(":forge:", "\uF007"),
            Pair(":Forgeisgarbage:", "\uF008"),
            Pair(":LUL1:", "\uF009"),
            Pair(":LUL2:", "\uF010"),
            Pair(":not_know:", "\uF011"),
            Pair(":oao_light:", "\uF012"),
            Pair(":oao_dark:", "\uF013"),
            Pair(":black_question:", "\uF014"),
            Pair(":rpmtw_team_logo:", "\uF015"),
            Pair(":RPMTW_logo_gif:", "\uF015"),
            Pair(":RPMLauncher:", "\uF016"),
            Pair(":dangerous:", "\uF017"),
            Pair(":fear:", "\uF018"),
            Pair(":check_star:", "\uF019"),
            Pair(":yellow_thinking:", "\uF020"),
            Pair(":SiongSng:", "\uF021"),
            Pair(":rpmwiki_logo:", "\uF022"),
            Pair(":rpmwiki_logo_complex:", "\uF023"),
            Pair(":rpmtw_platform_mod:", "\uF024"),
            Pair(":quilt:", "\uF025"),
        )
        emojiMap.forEach {
            result = result.replace(it.key, it.value)
        }
        return result
    }

    private fun formatMessage(message: String): MutableComponent {
        return formatUrl(ApiClientUtil.markdownToMinecraftFormatting(formatEmoji(message)))
    }

    private fun formatAuthorName(message: UniverseChatMessage): String {
        return if (message.nickname != null && message.nickname!!.isNotEmpty()) "${message.username} (${message.nickname})" else message.username
    }

    private fun listenMessages() {
        if (client.universeChatResource.isConnected) {
            client.universeChatResource.onMessageSent({ msg ->
                if (RPMTWConfig.get().universeChat.enable && RPMTWConfig.get().universeChat.enableReceiveMessage) {
                    Util.coroutineLaunch {
                        val isBlocked = msg.userIdentifier in RPMTWConfig.get().universeChat.blockUsers

                        if (isBlocked) return@coroutineLaunch

                        val isReply: Boolean = msg.replyMessageUUID != null

                        val component: MutableComponent = TextComponent.EMPTY.copy()
                        val title =
                            TextComponent("[${I18n.get("universeChat.rpmtw_platform_mod.title")}] ").setStyle(
                                Style.EMPTY.withColor(ChatFormatting.BLUE)
                            )
                        val authorName: String = formatAuthorName(msg)
                        val authorStyle: Style = Style.EMPTY.withClickEvent(
                            msg.avatarUrl?.let {
                                ClickEvent(
                                    ClickEvent.Action.OPEN_URL, it
                                )
                            }
                        ).withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                TranslatableComponent("universeChat.rpmtw_platform_mod.open_avatar_url")
                            )
                        )
                        val author = TextComponent("§e<§6${authorName}§e> ").setStyle(authorStyle)

                        val messageAction: MutableComponent =
                            TextComponent("  [-]")

                        messageAction.style = messageAction.style.withColor(ChatFormatting.GREEN).withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND, "/rpmtw universeChatAction ${msg.uuid}"
                            )
                        ).withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT, TextComponent(
                                    I18n.get("universeChat.rpmtw_platform_mod.gui.action")
                                )
                            )
                        )

                        val messageContent = formatMessage(msg.message)
                        val message: MutableComponent = TextComponent.EMPTY.copy()
                        if (isReply) {
                            val replyMessage: UniverseChatMessage? = getMessageAsync(msg.replyMessageUUID!!)

                            if (replyMessage != null) {
                                val replyAuthorName: String = if (msg.avatarUrl == replyMessage.avatarUrl) {
                                    I18n.get("gui.rpmtw_platform_mod.self")
                                } else {
                                    formatAuthorName(replyMessage)
                                }

                                val replyMessageContent = formatMessage(replyMessage.message)

                                message.append(
                                    "§a${I18n.get("gui.rpmtw_platform_mod.reply")} §6${
                                        replyAuthorName
                                    } §b"
                                )
                                message.append(replyMessageContent)
                                message.append(" §a-> ")
                                message.append(messageContent)
                            }
                        } else {
                            message.append(messageContent)
                        }

                        component.append(title)
                        component.append(author)
                        component.append(message)
                        component.append(
                            TextComponent.EMPTY.copy().setStyle(
                                Style.EMPTY.withClickEvent(
                                    ClickEvent(
                                        ClickEvent.Action.OPEN_URL,
                                        "rpmtw_universe_chat_avatar_url:${msg.avatarUrl}"
                                    )
                                )
                            )
                        )
                        component.append(messageAction)
                        // Message format
                        // [Universe Chat] <Steve> Hello World!  [Reply]
                        // Reply message format
                        // [Universe Chat] <Steve> Reply Alex Hi! -> Hello World!  [Reply]
                        Minecraft.getInstance().player?.displayClientMessage(component, false)
                    }
                }
            })
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to universe chat server failed")
        }
    }

    private fun statusHandler(status: String) {
        val prefix = "§9[${I18n.get("universeChat.rpmtw_platform_mod.title")}]§r"
        when (status) {
            "success" -> {
                Util.sendMessage(
                    "$prefix ${I18n.get("universeChat.rpmtw_platform_mod.status.success")}", overlay = true
                )
            }

            "phishing" -> {
                Util.sendMessage(
                    "$prefix ${I18n.get("universeChat.rpmtw_platform_mod.status.phishing")}", overlay = true
                )
            }

            "banned" -> {
                Util.sendMessage(
                    "$prefix ${I18n.get("universeChat.rpmtw_platform_mod.status.banned")}", overlay = true
                )
            }

            "unauthorized" -> {
                Util.sendMessage(
                    "$prefix ${I18n.get("universeChat.rpmtw_platform_mod.status.unauthorized")}", overlay = true
                )
            }
        }
    }

    private suspend fun sendMessage(message: String) {
        val status: String = client.universeChatResource.sendMessage(message, nickname = nickname)
        statusHandler(status)
    }

    private suspend fun replyMessage(message: String, uuid: String) {
        val status: String =
            client.universeChatResource.replyMessage(message = message, uuid = uuid, nickname = nickname)
        statusHandler(status)
    }

    fun handle() {
        if (RPMTWConfig.get().universeChat.enable) {
            RPMTWPlatformMod.LOGGER.info("Initializing universe chat server...")
            Util.coroutineLaunch {
                init()
                listen()
            }

            RPMTWPlatformMod.LOGGER.info("Universe chat initialized!")
        }
    }

    private fun listen() {
        val result: Deferred<Unit> = Util.coroutineAsync {
            listenMessages()
        }
        Util.coroutineLaunch {
            result.await()
        }
    }

    fun send(message: String) {
        if (client.universeChatResource.isConnected) {
            Util.coroutineLaunch {
                sendMessage(message)
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to universe chat server failed")
        }
    }

    fun reply(message: String, uuid: String) {
        if (client.universeChatResource.isConnected) {
            Util.coroutineLaunch {
                replyMessage(message, uuid)
            }
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to universe chat server failed")
        }
    }

    suspend fun getMessageAsync(uuid: String): UniverseChatMessage? {
        if (client.universeChatResource.isConnected) {
            val result: Deferred<UniverseChatMessage?> = Util.coroutineAsync {
                try {
                    return@coroutineAsync client.universeChatResource.getMessage(uuid)
                } catch (e: Exception) {
                    RPMTWPlatformMod.LOGGER.error("Failed to get message", e)
                    return@coroutineAsync null
                }
            }

            return result.await()
        } else {
            RPMTWPlatformMod.LOGGER.error("Connecting to universe chat server failed")
            return null
        }
    }

    fun close() {
        client.universeChatResource.disconnect()
    }

    fun restart() {
        RPMTWPlatformMod.LOGGER.info("Restarting universe chat...")
        close()
        handle()
        RPMTWPlatformMod.LOGGER.info("Universe chat restart!")
    }
}