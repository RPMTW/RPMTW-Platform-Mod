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
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

@Environment(EnvType.CLIENT)
class OnItemTooltip : ClientTooltipEvent.Item {
    private fun machineTranslation(itemStack: ItemStack, lines: MutableList<Component>) {
        try {
            val playing = Minecraft.getInstance().player != null
            if (playing) {
                fun load(index: Int, i18nKey: String, vararg i18nArgs: Any? = arrayOf()) {
                    if (index == -1) return

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
                    val unModifyLines = mutableListOf<Component>()
                    unModifyLines.addAll(lines)

                    for (line in unModifyLines) {
                        val index = unModifyLines.indexOf(line)

                        if (index == 0) {
                            load(0, itemKey)
                        }

                        val contents = line.contents
                        if (contents is TranslatableContents) {
                            load(index, contents.key, *contents.args)
                        }
                    }
                }

                // Check if the feature for unlocalized names is enabled and differs from translation
                if (RPMTWConfig.get().translate.unlocalized && unlocalizedName != itemStack.displayName.string) {
                    lines.add(1, Component.literal(unlocalizedName).withStyle(ChatFormatting.GRAY))
                }
            }

        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Error in item tooltip event", e)
        }
    }

    override fun append(itemStack: ItemStack?, lines: MutableList<Component>?, flag: TooltipFlag?) {
        if (lines != null && itemStack != null && lines.isNotEmpty()) {
            machineTranslation(itemStack, lines)
        }
    }
}