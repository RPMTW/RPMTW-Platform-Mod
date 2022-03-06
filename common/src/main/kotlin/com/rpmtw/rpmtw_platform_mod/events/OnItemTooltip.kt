package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MachineTranslationManager
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MachineTranslationStorage
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
            if (RPMTWConfig.get().translate.machineTranslation && playing && lines is ArrayList) {
                val unlocalizedName: String =
                    MachineTranslationStorage.getUnlocalizedTranslate(itemStack.descriptionId) ?: return

                // Check if the feature for unlocalized names is enabled and differs from translation
                if (RPMTWConfig.get().translate.unlocalized && unlocalizedName != itemStack.displayName.string) {
                    lines.add(1, TextComponent(unlocalizedName).withStyle(ChatFormatting.GRAY))
                }
                lines.add(
                    2,
                    TextComponent(
                        "按下 " + key.localizedName.string + " 後將物品機器翻譯為中文"
                    )
                )
                if (press) {
                    lines.add(2, MachineTranslationManager.createToolTip(unlocalizedName))
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