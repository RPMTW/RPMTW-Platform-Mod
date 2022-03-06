package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import net.minecraft.client.Minecraft

class OnClientStarted(val client: Minecraft) {
    init {
        CosmicChatHandler.handle()
    }
}