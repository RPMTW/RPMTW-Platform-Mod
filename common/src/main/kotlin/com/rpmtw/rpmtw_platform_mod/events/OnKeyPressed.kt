package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.ConfigObject
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import dev.architectury.event.EventResult
import dev.architectury.event.events.client.ClientRawInputEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@Environment(EnvType.CLIENT)
class OnKeyPressed : ClientRawInputEvent.KeyPressed {
    override fun keyPressed(
        client: Minecraft?,
        keyCode: Int,
        scanCode: Int,
        action: Int,
        modifiers: Int
    ): EventResult? {
        if (keyCode == -1 || scanCode == -1) return EventResult.pass()

        val bindings: ConfigObject.KeyBindings = RPMTWConfig.get().keyBindings
        if (bindings.config.matchesKey(keyCode, scanCode)) {
            client?.setScreen(RPMTWConfig.getScreen())
        } else if (bindings.reloadTranslatePack.matchesKey(keyCode, scanCode)) {
            try {
                TranslateResourcePack.load()
                RPMTWPlatformMod.LOGGER.info("Translate resource pack successful reloaded")
            } catch (e: Exception) {
                RPMTWPlatformMod.LOGGER.error("Failed to reload translate resource pack", e)
            }
        }

        return EventResult.pass()
    }
}