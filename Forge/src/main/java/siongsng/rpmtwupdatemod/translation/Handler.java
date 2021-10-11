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
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.lwjgl.input.Keyboard;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
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
        return Keyboard.isKeyDown(key.getKeyCode());
    }


    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        event.getToolTip().add(1, new TextComponentString("原文: " + Handler.getNoLocalizedMap().getOrDefault(stack.getTranslationKey(), "無")).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getMinecraft().player != null;
        if (RPMTWConfig.isTranslate && playing) {
            if (press) {
                for (ITextComponent text : TranslationManager.getInstance().createToolTip(stack)) {
                    event.getToolTip().add(2, text.getFormattedText());
                }
            } else {
                boolean isNoLocalized = getNoLocalizedMap().containsKey(stack.getTranslationKey());
                if (isNoLocalized) {
                    event.getToolTip().add(2, "按下 " + RPMKeyBinding.translate.getDisplayName() + " 後將物品機器翻譯為中文");
                }
            }
        }

    }

    @SubscribeEvent
    public void quit(final PlayerEvent.PlayerLoggedOutEvent event) {
        TranslationManager.getInstance().writeCash();
    }
}
