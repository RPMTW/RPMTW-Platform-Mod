package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.events.OnClientStopping
import dev.architectury.event.events.client.ClientLifecycleEvent

object EventHandler {
    fun handle() {
        ClientLifecycleEvent.CLIENT_STOPPING.register(::OnClientStopping)
    }
}