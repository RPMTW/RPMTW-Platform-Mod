package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.MachineTranslationText
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTStorage
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class OnItemTooltip(private val itemStack: ItemStack, private val lines: List<Component>, flag: TooltipFlag) {

    private fun onTooltip() {
        try {
            val key: ModifierKeyCode = RPMTWConfig.get().keyBindings.machineTranslation
            val press: Boolean = key.matchesCurrentKey()
            val playing = Minecraft.getInstance().player != null
            val unlocalizedName: String = MTStorage.getUnlocalizedTranslate(itemStack.descriptionId) ?: return

            // Check if the feature for unlocalized names is enabled and differs from translation
            if (playing && lines is ArrayList && unlocalizedName != itemStack.displayName.string) {

                if (RPMTWConfig.get().translate.unlocalized) {
                    lines.add(1, TextComponent(unlocalizedName).withStyle(ChatFormatting.GRAY))
                }
                if (RPMTWConfig.get().translate.machineTranslation) {
                    if (MTManager.getFromCache(unlocalizedName) != null) {
                        lines.removeAt(0)
                        lines.add(0, MTManager.getFromCache(unlocalizedName)!!)
                    } else {
                        if (press) {
                            lines.removeAt(0)
                            lines.add(0, MachineTranslationText(unlocalizedName))
                        } else {
                            MTManager.addToQueue(unlocalizedName)
                        }
                    }
                }
            }

        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Error in item tooltip event", e)
        }
    }

    init {
        onTooltip()
    }
}