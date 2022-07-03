@file:JvmName("RPMTWPlatformModPlugin")

package com.rpmtw.rpmtw_platform_mod

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
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

    @ExpectPlatform
    @JvmStatic
    fun <T> registerReloadEvent(reloadListener: SimplePreparableReloadListener<T>) {
        // Just throw an error, the content should get replaced at runtime.
        throw AssertionError()
    }

    @ExpectPlatform
    @JvmStatic
    fun getResourcePacksFolder(): File { throw AssertionError() }
}