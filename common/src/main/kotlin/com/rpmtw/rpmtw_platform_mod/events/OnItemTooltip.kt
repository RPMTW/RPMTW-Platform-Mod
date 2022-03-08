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
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class OnItemTooltip(private val itemStack: ItemStack, private val lines: List<Component>, flag: TooltipFlag) {

    private fun onTooltip() {
        try {
            val playing = Minecraft.getInstance().player != null
            if (playing && lines is ArrayList) {
                fun load(index: Int, i18nKey: String) {
                    val key: ModifierKeyCode = RPMTWConfig.get().keyBindings.machineTranslation
                    val press: Boolean = key.matchesCurrentKey()

                    // Check if it has been human translated
                    if (!MTStorage.isTranslate(i18nKey)) {
                        val unlocalizedName: String = MTStorage.getUnlocalizedTranslate(i18nKey) ?: return

                        if (MTManager.getFromCache(unlocalizedName) != null) {
                            lines.removeAt(index)
                            lines.add(index, MTManager.getFromCache(unlocalizedName)!!)
                        } else {
                            if (press) {
                                lines.removeAt(index)
                                lines.add(index, MachineTranslationText(unlocalizedName))
                            } else {
                                MTManager.addToQueue(unlocalizedName)
                            }
                        }
                    }
                }

                val itemKey: String = itemStack.descriptionId
                val unlocalizedName: String = MTStorage.getUnlocalizedTranslate(itemKey) ?: return

                if (RPMTWConfig.get().translate.machineTranslation) {
                    // Item name
                    load(0, itemKey)

                    for (i in 1 until lines.size) {
                        val line: Component = lines.getOrNull(i) ?: continue
                        if (line is TranslatableComponent) {
                            load(i, line.key)
                        }
                    }
                }

                val itemName: String = itemStack.displayName.string
                // Check if the feature for unlocalized names is enabled and differs from translation
                if (RPMTWConfig.get().translate.unlocalized && unlocalizedName != itemName) {
                    lines.add(TextComponent(unlocalizedName).withStyle(ChatFormatting.GRAY))
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