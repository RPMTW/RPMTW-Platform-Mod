package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.commands.CommandBuildContext
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor


@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun registerClientCommand(
        command: String,
        subCommand: String,
        argumentName: String? = null,
        argumentType: ArgumentType<*>? = null,
        executes: (CommandContext<*>) -> Int
    ) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandBuildContext? ->
            if (argumentName != null && argumentType != null) {
                dispatcher.register(
                    literal(command).then(
                        literal(subCommand).then(argument(argumentName, argumentType).executes {
                            return@executes executes(it)
                        })
                    )
                )
            } else {
                dispatcher.register(literal(command).then(literal(subCommand).executes {
                    return@executes executes(it)
                }))
            }
        })
    }

    @JvmStatic
    fun <T> registerReloadEvent(reloadListener: SimplePreparableReloadListener<T>) {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(PreparableReloadListenerWrapper(reloadListener))
    }
}

@Suppress("SpellCheckingInspection")
private class PreparableReloadListenerWrapper<T>(val reloadListener: SimplePreparableReloadListener<T>) :
    IdentifiableResourceReloadListener {

    override fun reload(
        preparationBarrier: PreparableReloadListener.PreparationBarrier,
        resourceManager: ResourceManager,
        profilerFiller: ProfilerFiller,
        profilerFiller2: ProfilerFiller,
        executor: Executor,
        executor2: Executor
    ): CompletableFuture<Void> {
        return reloadListener.reload(
            preparationBarrier,
            resourceManager,
            profilerFiller,
            profilerFiller2,
            executor,
            executor2
        )
    }

    override fun getFabricId(): ResourceLocation {
        return ResourceLocation("${RPMTWPlatformMod.MOD_ID}/reload_listener_${reloadListener.hashCode()}")
    }
}