package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import net.minecraft.commands.SharedSuggestionProvider

class LoginRPMTWAccountCommand : RPMTWCommand() {
    override fun register(): LiteralArgumentBuilder<SharedSuggestionProvider> {
        val command = literal("login").executes {
            RPMTWAuthHandler.login()

            return@executes CommandHandler.success
        }

        return command
    }
}