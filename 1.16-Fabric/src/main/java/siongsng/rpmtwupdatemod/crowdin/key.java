package siongsng.rpmtwupdatemod.crowdin;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class key {
    private static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.rpmtw");
    private static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.rpmtw");

    public static void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (crowdin.wasPressed()) {
                assert client.player != null;
                Item item = client.player.getMainHandStack().getItem();

                String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
                String item_key = item.getTranslationKey(); //物品的命名空間
                String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                if (item_key.equals("block.minecraft.air")) {
                    client.player.sendMessage(new LiteralText("§4請手持物品後再使用此功能。"), false);
                    return;
                }
                String msg = String.format( //訊息內容
                        "§c-------------------------\n" +
                                "§b模組ID: §a%s\n" +
                                "§b顯示名稱: §a%s\n" +
                                "§b命名空間: §a%s\n" +
                                "§c-------------------------", mod_id, item_DisplayName, item_key);
                client.player.sendMessage(new LiteralText(msg), false);

                String url = "https://translate.rpmtw.ga/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + item_key;
                client.player.sendMessage(new LiteralText("§6開啟翻譯平台網頁中..."), false);
                Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
            }
            while (reloadpack.wasPressed()) {
                MinecraftClient.getInstance().reloadResources();
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§6重新載入RPMTW繁體中文化資源包完畢"), false);
            }
        });
    }
}
