package siongsng.rpmtwupdatemod.crowdin;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.json;
import siongsng.rpmtwupdatemod.mixins.ResourcePackManagerMixin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class key {
    private static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.rpmtw");
    private static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.rpmtw");
    private static final KeyBinding report_translation = new KeyBinding("key.rpmtw_update_mod.report_translation", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.categories.rpmtw");

    public static void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);
        KeyBindingHelper.registerKeyBinding(report_translation);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (crowdin.wasPressed()) {
                assert client.player != null;
                Item item = client.player.getMainHandStack().getItem();

                String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
                String item_key = item.getTranslationKey(); //物品的命名空間
                String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                // String item_not_localized = ; //物品尚未翻譯的名稱
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
                try {
                    FileUtils.copyURLToFile(new URL(json.loadJson().toString()), Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16/RPMTW-1.16.zip").toFile()); //下載資源包檔案
                    Class.forName("siongsng.rpmtwupdatemod.packs.LoadPack").getMethod("init", Set.class).invoke(null, new HashSet(new HashSet()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                MinecraftClient.getInstance().reloadResources();
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§6重新下載最新資源包並且載入RPMTW繁體中文化資源包完畢"), false);
            }
            while (report_translation.wasPressed()) {
                assert client.player != null;
                Item item = client.player.getMainHandStack().getItem();

                String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
                String item_key = item.getTranslationKey(); //物品的命名空間
                String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                if (item_key.equals("block.minecraft.air")) {
                    client.player.sendMessage(new LiteralText("§4請手持要回報翻譯錯誤的物品或方塊..."), false);
                    return;
                }

                String url = String.format("https://docs.google.com/forms/d/e/1FAIpQLSelkP16fMms-_3q4ewdVLaDO14YdmmupcZ2Yl1V0sPtuC-v_g/viewform?usp=pp_url&entry.412976727=%s&entry.2706446=%s", mod_id, item_key);
                client.player.sendMessage(new LiteralText((String.format("§6即將開啟回報錯誤的網頁中...\n§9回報的物品: §e%s", item_DisplayName))), false);
                Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
            }
        });
    }
}
