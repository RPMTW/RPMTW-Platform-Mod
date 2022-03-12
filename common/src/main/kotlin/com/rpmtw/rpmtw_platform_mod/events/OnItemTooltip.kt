package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.gui.widgets.MachineTranslationText
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTStorage
import dev.architectury.event.events.client.ClientTooltipEvent
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

@Environment(EnvType.CLIENT)
class OnItemTooltip : ClientTooltipEvent.Item {
    private fun machineTranslation(itemStack: ItemStack, lines: MutableList<Component>) {
        try {
            val playing = Minecraft.getInstance().player != null
            if (playing) {
                fun load(index: Int, i18nKey: String, vararg i18nArgs: Any? = arrayOf()) {
                    val key: ModifierKeyCode = RPMTWConfig.get().keyBindings.machineTranslation
                    val press: Boolean = key.matchesCurrentKey()

                    // Check if it has been human translated
                    if (!MTStorage.isTranslate(i18nKey)) {
                        val unlocalizedName: String = MTStorage.getUnlocalizedTranslate(i18nKey) ?: return

                        if (MTManager.getFromCache(unlocalizedName) != null) {
                            lines.removeAt(index)
                            lines.add(index, MTManager.getFromCache(unlocalizedName, *i18nArgs)!!)
                        } else {
                            if (press) {
                                lines.removeAt(index)
                                lines.add(index, MachineTranslationText(unlocalizedName, *i18nArgs))
                            } else if (RPMTWConfig.get().translate.autoMachineTranslation) {
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

                            load(i, line.key, *line.args)
                        }
                    }
                }

                val itemName: String = itemStack.displayName.string
                // Check if the feature for unlocalized names is enabled and differs from translation
                if (RPMTWConfig.get().translate.unlocalized && unlocalizedName != itemName) {
                    lines.add(1, TextComponent(unlocalizedName).withStyle(ChatFormatting.GRAY))
                }
            }

        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Error in item tooltip event", e)
        }
    }

    override fun append(itemStack: ItemStack?, lines: MutableList<Component>?, flag: TooltipFlag?) {
        if (lines != null && itemStack != null) {
            machineTranslation(itemStack, lines)
        }
    }
}