package com.rpmtw.rpmtw_platform_mod.events

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MachineTranslationStorage
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class OnItemTooltip(stack: ItemStack, lines: List<Component>, flag: TooltipFlag) {

    private fun onTooltip(itemStack: ItemStack, lines: MutableList<Component>) {
        try {
            val key: ModifierKeyCode = RPMTWConfig.get().keyBindings.machineTranslation
            val press: Boolean = key.matchesCurrentKey()
            val playing = Minecraft.getInstance().player != null
            if (RPMTWConfig.get().translate.machineTranslation && playing) {
                val source: String = MachineTranslationStorage.getUnlocalizedTranslate(itemStack.descriptionId) ?: "無"
                if (RPMTWConfig.get().translate.unlocalized){
                    lines.add(1, TextComponent("原文: $source").withStyle(ChatFormatting.GRAY))
                }
                lines.add(
                    2,
                    TextComponent(
                        "按下 " + key.localizedName.string + " 後將物品機器翻譯為中文"
                    )
                )
                if (press) {
                    RPMTWPlatformMod.LOGGER.info("key pressed")
//                    for (text in TranslationManager.getInstance().createToolTip(source)) {
//                        lines.add(2, text)
//                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }


    init {
        onTooltip(stack, lines.toMutableList())
    }
}