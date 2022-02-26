package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_api_client.RPMTWApiClient
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import net.minecraft.client.User
import kotlin.coroutines.CoroutineContext

object CosmicChatHandler {
    private val client: RPMTWApiClient = RPMTWApiClient.instance
    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job()
    }

    private suspend fun init() {
        val minecraft: Minecraft = Minecraft.getInstance()
        val user: User = minecraft.user
        client.cosmicChatResource.connect(minecraftUUID = user.uuid)
    }

    fun handle() {
        if (RPMTWConfig.get().cosmicChat.enable) {
            RPMTWPlatformMod.LOGGER.info("Initializing Cosmic Chat...")
            val result: Deferred<Unit> = coroutineScope.async {
                init()
            }
            coroutineScope.launch {
                result.await()
            }

            RPMTWPlatformMod.LOGGER.info("Cosmic Chat initialized!")
        }
    }

    fun close() {
        client.cosmicChatResource.disconnect()
    }
}