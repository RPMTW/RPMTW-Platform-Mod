package com.rpmtw.rpmtw_platform_mod.fabric

import com.mojang.brigadier.CommandDispatcher
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor


@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfigScreen() {
        // no-op
    }

    @JvmStatic
    fun dispatchClientCommand(callback: (dispatcher: CommandDispatcher<SharedSuggestionProvider>, buildContext: CommandBuildContext) -> Unit) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, buildContext: CommandBuildContext ->
            @Suppress("UNCHECKED_CAST")
            callback(dispatcher as CommandDispatcher<SharedSuggestionProvider>, buildContext)
        })
    }

    @JvmStatic
    fun <T> registerReloadEvent(reloadListener: SimplePreparableReloadListener<T>) {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(PreparableReloadListenerWrapper(reloadListener))
    }

    @JvmStatic
    fun executeClientCommand(command: String): Boolean {
        return ClientCommandInternals.executeCommand(command)
    }

    @JvmStatic
    fun getGameFolder(): File {
        return FabricLoader.getInstance().gameDir.toFile()
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