package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.ConfigObject
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack
import com.rpmtw.rpmtw_platform_mod.util.Util
import dev.architectury.event.EventResult
import dev.architectury.event.events.client.ClientRawInputEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.I18n
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import org.lwjgl.glfw.GLFW

@Environment(EnvType.CLIENT)
class OnKeyPressed : ClientRawInputEvent.KeyPressed {
    override fun keyPressed(
        client: Minecraft,
        keyCode: Int,
        scanCode: Int,
        action: Int,
        modifiers: Int
    ): EventResult? {
        // Ignore unknown keys.
        if (keyCode == -1 || scanCode == -1) return EventResult.pass()

        // Check whether the key is pressed to prevent multiple calls.
        if (action != GLFW.GLFW_PRESS) return EventResult.pass()

        val bindings: ConfigObject.KeyBindings = RPMTWConfig.get().keyBindings
        if (bindings.config.matchesKey(keyCode, scanCode)) {
            openConfigScreen(client)
        } else if (bindings.reloadTranslatePack.matchesKey(keyCode, scanCode)) {
            reloadTranslatePack()
        } else if (bindings.openCrowdinPage.matchesKey(keyCode, scanCode)) {
            openCrowdinPage(client)
        }

        return EventResult.pass()
    }

    private fun openConfigScreen(client: Minecraft) {
        client.setScreen(RPMTWConfig.getScreen())
    }

    private fun reloadTranslatePack() {
        try {
            TranslateResourcePack.reload()
            RPMTWPlatformMod.LOGGER.info("Translate resource pack successful reloaded")
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to reload translate resource pack", e)
        }
    }

    private fun openCrowdinPage(client: Minecraft) {
        try {
            val player = client.player
            val world = client.level

            // To check whether the player is in the world.
            if (player != null && world != null) {
                val item = player.mainHandItem.item
                var i18nKey: String = item.descriptionId

                // if the player's main hand item doesn't have anything, try to get the item from the block or entity that the player is looking at.
                if (i18nKey == "block.minecraft.air") {
                    when (val hitResult = client.hitResult) {
                        is BlockHitResult -> {
                            val block = world.getBlockState(hitResult.blockPos).block
                            i18nKey = block.asItem().descriptionId
                        }

                        is EntityHitResult -> {
                            SpawnEggItem.byId(hitResult.entity.type)?.let {
                                i18nKey = it.descriptionId
                            }
                        }
                    }

                    if (i18nKey == "block.minecraft.air") {
                        Util.sendMessage(I18n.get("config.rpmtw_platform_mod.option.keyBindings.openCrowdinPage.nothing"))
                        return
                    }
                }

                Util.openLink("https://crowdin.com/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=$i18nKey")
            }
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to open the crowdin page", e)
        }
    }
}