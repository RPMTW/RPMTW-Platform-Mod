package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import dev.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@Environment(EnvType.CLIENT)
class OnClientSetup : ClientLifecycleEvent.ClientState {
    override fun stateChanged(instance: Minecraft?) {
        if (instance != null) {
            RPMTWConfig.register()
            if (RPMTWConfig.get().translate.loadTranslateResourcePack) {
                TranslateResourcePack.load()
            }
        }
    }
}