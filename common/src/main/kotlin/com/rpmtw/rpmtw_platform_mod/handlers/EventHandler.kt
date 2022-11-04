package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.events.*
import dev.architectury.event.events.client.ClientGuiEvent
import dev.architectury.event.events.client.ClientLifecycleEvent
import dev.architectury.event.events.client.ClientRawInputEvent
import dev.architectury.event.events.client.ClientTooltipEvent

object EventHandler {
    fun handle() {
        ClientLifecycleEvent.CLIENT_STOPPING.register(OnClientStopping())
        ClientLifecycleEvent.CLIENT_STARTED.register(OnClientStarted())
        ClientGuiEvent.INIT_POST.register(OnGuiInitPost())
        ClientTooltipEvent.ITEM.register(OnItemTooltip())
        ClientRawInputEvent.KEY_PRESSED.register(OnKeyPressed())
        RPMTWPlatformModPlugin.registerReloadEvent(OnReloadResource())
    }
}