package siongsng.rpmtwupdatemod.translation;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;

import java.util.HashMap;
import java.util.Map;

public class Handler {
    private static final Map<String, String> noLocalizedMap = new HashMap<>();


    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }

    public boolean isKeyPress(KeyMapping key) {
        try {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
        } catch (Exception exception) {
            return false;
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getInstance().player != null;
        if (RPMTWConfig.isTranslate.get() && playing) {
            ItemStack stack = event.getItemStack();
            String source = noLocalizedMap.getOrDefault(stack.getDescriptionId(), "無");
            event.getToolTip().add(1, new TextComponent("原文: " + source).withStyle(ChatFormatting.GRAY));
            if (press) {
                for (Component text : TranslationManager.getInstance().createToolTip(source)) {
                    event.getToolTip().add(2, text);
                }
            } else {
                event.getToolTip().add(2, new TextComponent("按下 " + RPMKeyBinding.translate.getTranslatedKeyMessage().getString() + " 後將物品機器翻譯為中文"));
            }
        }

    }
}
