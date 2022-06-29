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
import net.minecraft.client.resources.language.I18n

@Environment(EnvType.CLIENT)
class OnKeyPressed : ClientRawInputEvent.KeyPressed {
    override fun keyPressed(
        client: Minecraft?,
        keyCode: Int,
        scanCode: Int,
        action: Int,
        modifiers: Int
    ): EventResult? {
        val bindings: ConfigObject.KeyBindings = RPMTWConfig.get().keyBindings
        if (bindings.config.matchesKey(keyCode, modifiers)) {
            client?.setScreen(RPMTWConfig.getScreen())
        }

        return EventResult.pass()
    }
}