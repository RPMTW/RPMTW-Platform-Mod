package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@Suppress("UNUSED_PARAMETER")
@Environment(EnvType.CLIENT)
class OnClientStopping(minecraft: Minecraft) {
    init {
        // Close the cosmic chat socket to save resources.
        CosmicChatHandler.close()
    }
}