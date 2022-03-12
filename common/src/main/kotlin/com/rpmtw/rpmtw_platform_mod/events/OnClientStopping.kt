package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import dev.architectury.event.events.client.ClientLifecycleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@Suppress("UNUSED_PARAMETER")
@Environment(EnvType.CLIENT)
class OnClientStopping : ClientLifecycleEvent.ClientState {
    override fun stateChanged(instance: Minecraft?) {
        if (instance != null) {
            MTManager.saveCache()
            // Close the cosmic chat socket to save resources.
            CosmicChatHandler.close()
            RPMTWPlatformMod.LOGGER.info("Cosmic chat socket closed.")
        }
    }
}