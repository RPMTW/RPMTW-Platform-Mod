package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

class OnPlayerJoin : PlayerEvent.PlayerJoin {
    override fun join(player: ServerPlayer) {
        if (!RPMTWConfig.get().firstJoinLevel) return

        val officailWebsiteLink = Component.literal("rpmtw.com").withStyle {
            it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://rpmtw.com")).withColor(ChatFormatting.BLUE)
        }
        val officialDiscordLink = Component.literal("discord.gg/5xApZtgV2u").withStyle {
            it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/5xApZtgV2u")).withColor(ChatFormatting.BLUE)
        }
        val message = Component.translatable("messages.rpmtw_platform_mod.thanks", officailWebsiteLink, officialDiscordLink)

        player.sendSystemMessage(message)

        // RPMTWConfig.get().firstJoinLevel = false
    }
}