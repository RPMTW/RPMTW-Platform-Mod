@file:JvmName("RPMTWPlatformModPlugin")

package com.rpmtw.rpmtw_platform_mod

import com.mojang.brigadier.CommandDispatcher
import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.SharedSuggestionProvider
import java.io.File

@Suppress("UNUSED_PARAMETER")
object RPMTWPlatformModPlugin {
    @ExpectPlatform
    @JvmStatic
    fun registerConfigScreen() {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @JvmStatic
    @ExpectPlatform
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>, buildContext: CommandBuildContext) -> Unit) {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @ExpectPlatform
    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @ExpectPlatform
    @JvmStatic
    fun getGameFolder(): File { throw AssertionError() }
}