package siongsng.rpmtwupdatemod.translation;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;

import java.util.HashMap;
import java.util.Map;

public class Handler {
    private static Map<String, String> noLocalizedMap = new HashMap<>();

    public static Map<String, String> getNoLocalizedMap() {
        return noLocalizedMap;
    }

    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }


    public boolean isKeyPress(KeyBinding key) {
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), key.getKey().getKeyCode());
    }


    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        event.getToolTip().add(1, new StringTextComponent("原文: " + Handler.getNoLocalizedMap().getOrDefault(stack.getTranslationKey(), "無")).mergeStyle(TextFormatting.GRAY));
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getInstance().player != null;
        if (Configer.isTranslate.get() && playing) {
            if (press) {
                for (ITextComponent text : TranslationManager.getInstance().createToolTip(stack)) {
                    event.getToolTip().add(2, text);
                }
            } else {
                boolean isNoLocalized = getNoLocalizedMap().containsKey(stack.getTranslationKey());
                if (isNoLocalized) {
                    event.getToolTip().add(2, new StringTextComponent("按下 " + RPMKeyBinding.translate.getDefault().func_237520_d_().getString() + " 後將物品機器翻譯為中文"));
                }
            }
        }

    }
}
