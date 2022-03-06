@file:JvmName("RPMTWPlatformModPlugin")

package com.rpmtw.rpmtw_platform_mod

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.architectury.injectables.annotations.ExpectPlatform

object RPMTWPlatformModPlugin {
    @ExpectPlatform
    @JvmStatic
    fun registerConfigScreen() {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @JvmStatic
    @ExpectPlatform
    fun registerClientCommand(command: String, executes: () -> Int) {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @JvmStatic
    @ExpectPlatform
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String? = null,
        argumentType: ArgumentType<*>? = null,
        executes: (CommandContext<*>) -> Int
    ) {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }
}