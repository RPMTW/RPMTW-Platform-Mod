package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.SharedSuggestionProvider

abstract class RPMTWCommand {
    abstract fun register(): LiteralArgumentBuilder<SharedSuggestionProvider>
}