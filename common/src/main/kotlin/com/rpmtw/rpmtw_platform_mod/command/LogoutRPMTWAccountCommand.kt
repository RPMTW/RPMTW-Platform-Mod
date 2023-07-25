package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import net.minecraft.commands.SharedSuggestionProvider

class LogoutRPMTWAccountCommand : RPMTWCommand() {
    override fun register(): LiteralArgumentBuilder<SharedSuggestionProvider> {
        val command = literal("logout").executes {
            RPMTWAuthHandler.logout()

            return@executes CommandHandler.success
        }

        return command
    }
}