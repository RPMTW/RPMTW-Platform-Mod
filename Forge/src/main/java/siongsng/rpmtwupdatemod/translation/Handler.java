package siongsng.rpmtwupdatemod.translation;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;

import java.util.HashMap;
import java.util.Map;

public class Handler {
    private static final Map<String, String> noLocalizedMap = new HashMap<>();

    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }


    public boolean isKeyPress(KeyBinding key) {
        try {
            return Keyboard.isKeyDown(key.getKeyCode());
        } catch (Exception exception) {
            return false;
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        String source = noLocalizedMap.getOrDefault(stack.getTranslationKey(), noLocalizedMap.getOrDefault(stack.getTranslationKey() + ".name", "無"));
        event.getToolTip().add(1, new TextComponentString("原文: " + source).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getMinecraft().player != null;
        if (RPMTWConfig.isTranslate && playing) {
            if (press) {
                for (ITextComponent text : TranslationManager.getInstance().createToolTip(source)) {
                    event.getToolTip().add(2, text.getFormattedText());
                }
            } else {
                event.getToolTip().add(2, "按下 " + RPMKeyBinding.translate.getDisplayName() + " 後將物品機器翻譯為中文");
            }
        }

    }
}
