package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer

class OnPlayerJoin : PlayerEvent.PlayerJoin {
    override fun join(player: ServerPlayer) {
        if (!RPMTWConfig.get().firstJoinLevel) return

        val officailWebsiteLink = TextComponent("https://rpmtw.com").withStyle {
            it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://rpmtw.com"))
                .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, TranslatableComponent("gui.rpmtw_platform_mod_open_link")))
                .withColor(ChatFormatting.BLUE)
        }
        val officialDiscordLink = TextComponent("https://discord.gg/5xApZtgV2u").withStyle {
            it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/5xApZtgV2u"))
                .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, TranslatableComponent("gui.rpmtw_platform_mod_open_link")))
                .withColor(ChatFormatting.BLUE)
        }
        val settingsKey = RPMTWConfig.get().keyBindings.config.localizedName.copy().withStyle {
            it.withColor(ChatFormatting.RED)
        }

        val message = TranslatableComponent("messages.rpmtw_platform_mod.thanks", officailWebsiteLink, officialDiscordLink, settingsKey)

        player.displayClientMessage(message, false)
        RPMTWConfig.get().firstJoinLevel = false
    }
}