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
import siongsng.rpmtwupdatemod.Register.RPMKeyBinding;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    private static final Map<String, String> noLocalizedMap = new HashMap<>();

    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }

    public static boolean isKeyPress(KeyBinding key) {
        try {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyCodeAccessor) key).fabric_getBoundKey().getCode());
        } catch (Exception exception) {
            return false;
        }
    }

    public static void init() {
        ItemTooltipCallback.EVENT.register(Handler::onTooltip);
    }

    public static void onTooltip(ItemStack itemStack, TooltipContext tooltipFlag, List<Text> list) {
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = MinecraftClient.getInstance().player != null;

        if (playing && RPMTWConfig.getConfig().isTranslate) {
            String source = noLocalizedMap.getOrDefault(itemStack.getTranslationKey(), "無");
            list.add(1, new LiteralText("原文: " + source).formatted(Formatting.GRAY));
            if (press) {
                for (Text text : TranslationManager.getInstance().createToolTip(source)) {
                    list.add(2, text);
                }
            } else {
                list.add(2, new LiteralText("按下 " + RPMKeyBinding.translate.getBoundKeyLocalizedText().asString() + " 後將物品機器翻譯為中文"));
            }
        }

    }
}
