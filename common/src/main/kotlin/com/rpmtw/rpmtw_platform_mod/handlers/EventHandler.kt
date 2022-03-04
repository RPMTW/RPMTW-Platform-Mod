package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.events.OnClientStarted
import com.rpmtw.rpmtw_platform_mod.events.OnClientStopping
import com.rpmtw.rpmtw_platform_mod.events.OnGuiInitPost
import dev.architectury.event.events.client.ClientGuiEvent
import dev.architectury.event.events.client.ClientLifecycleEvent

object EventHandler {
    fun handle() {
        ClientLifecycleEvent.CLIENT_STOPPING.register(::OnClientStopping)
        ClientLifecycleEvent.CLIENT_STARTED.register(::OnClientStarted)
        try {
            ClientGuiEvent.INIT_POST.register(::OnGuiInitPost)
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Adding button to the chat screen failed", e)
        }
    }
}