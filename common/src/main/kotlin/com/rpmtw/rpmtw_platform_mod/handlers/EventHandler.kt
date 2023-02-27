package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.events.*
import me.shedaniel.architectury.event.events.GuiEvent
import me.shedaniel.architectury.event.events.TooltipEvent
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent
import me.shedaniel.architectury.event.events.client.ClientRawInputEvent

object EventHandler {
    fun handle() {
        ClientLifecycleEvent.CLIENT_STOPPING.register(OnClientStopping())
        ClientLifecycleEvent.CLIENT_STARTED.register(OnClientStarted())
        GuiEvent.INIT_POST.register(OnGuiInitPost())
        TooltipEvent.ITEM.register(OnItemTooltip())
        ClientRawInputEvent.KEY_PRESSED.register(OnKeyPressed())
        PlayerEvent.PLAYER_JOIN.register(OnPlayerJoin())
    }
}