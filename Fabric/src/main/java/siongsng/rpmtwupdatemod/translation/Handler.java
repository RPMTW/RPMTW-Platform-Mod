package siongsng.rpmtwupdatemod.translation;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    private static Map<String, String> noLocalizedMap = new HashMap<>();

    public static Map<String, String> getNoLocalizedMap() {
        return noLocalizedMap;
    }

    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }


    public static boolean isKeyPress(KeyBinding key) {
        try {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyCodeAccessor) key).fabric_getBoundKey().getCode());
        } catch (Exception e) {
            return false;
        }
    }

    public static void init() {
        ItemTooltipCallback.EVENT.register(Handler::onTooltip);
    }

    public static void onTooltip(ItemStack itemStack, TooltipContext tooltipFlag, List<Text> list) {
        list.add(1, new LiteralText("原文: " + Handler.getNoLocalizedMap().getOrDefault(itemStack.getTranslationKey(), "無")).formatted(Formatting.GRAY));
        boolean press = isKeyPress(key.translate);
        boolean playing = MinecraftClient.getInstance().player != null;
        if (playing && RPMTWConfig.getConfig().isTranslate) {
            if (press) {
                for (Text text : TranslationManager.getInstance().createToolTip(itemStack)) {
                    list.add(2, text);
                }
            } else {
                boolean isNoLocalized = getNoLocalizedMap().containsKey(itemStack.getTranslationKey());
                if (isNoLocalized) {
                    list.add(2, new LiteralText("按下 " + key.translate.getBoundKeyLocalizedText().asString() + " 後將物品機器翻譯為中文"));
                }
            }
        }
    }
}
