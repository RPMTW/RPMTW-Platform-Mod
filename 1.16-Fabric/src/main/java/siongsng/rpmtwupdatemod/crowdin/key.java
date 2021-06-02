package siongsng.rpmtwupdatemod.crowdin;

import me.shedaniel.autoconfig.AutoConfig;
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
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.*;

public class key {
    private static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    private static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    private static final KeyBinding report_translation = new KeyBinding("key.rpmtw_update_mod.report_translation", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.categories.rpmtw");
    private static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");

    static ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();

    public static void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);
        KeyBindingHelper.registerKeyBinding(report_translation);
        KeyBindingHelper.registerKeyBinding(open_config);


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (open_config.wasPressed()) {
                MinecraftClient.getInstance().openScreen(AutoConfig.getConfigScreen(ConfigScreen.class, MinecraftClient.getInstance().currentScreen).get());
            }
            if (config.crowdin) {
                while (crowdin.wasPressed()) {
                    assert client.player != null;
                    Item item = client.player.getMainHandStack().getItem();
                    String item_key = item.getTranslationKey(); //物品的命名空間
                    if (item_key.equals("block.minecraft.air")) {
                        SendMsg.send("§4請手持物品後再使用此功能。");
                        return;
                    } else if (!config.isCheck) {
                        MinecraftClient.getInstance().openScreen(new CrowdinLoginScreen(new CrowdinLogin()));
                        return;
                    } else {
                        SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                        Thread thread = new Thread(() -> {
                            if (CrowdinGuiProcedure.getText() == null && config.isCheck) {
                                SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                                return;
                            }
                            MinecraftClient.getInstance().openScreen(new CrowdinGuiScreen(new CrowdinGui()));
                        });
                        thread.start();
                    }
                }
            }
            if (config.ReloadPack) {
                while (reloadpack.wasPressed()) {
                    new ReloadPack();
                }
            }
            if (config.ReportTranslation) {
                while (report_translation.wasPressed()) {
                    assert client.player != null;
                    Item item = client.player.getMainHandStack().getItem();

                    String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
                    String item_key = item.getTranslationKey(); //物品的命名空間
                    String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                    String Game_ver = "Fabric-" + MinecraftClient.getInstance().getGame().getVersion().getReleaseTarget(); //遊戲版本
                    if (item_key.equals("block.minecraft.air")) {
                        client.player.sendMessage(new LiteralText("§4請手持要回報翻譯錯誤的物品或方塊..."), false);
                        return;
                    }

                    String url = String.format("https://docs.google.com/forms/d/e/1FAIpQLSelkP16fMms-_3q4ewdVLaDO14YdmmupcZ2Yl1V0sPtuC-v_g/viewform?usp=pp_url&entry.1886547466=%s&entry.412976727=%s&entry.2706446=%s", Game_ver, mod_id, item_key);
                    client.player.sendMessage(new LiteralText((String.format("§6即將開啟回報錯誤的網頁中...\n§9回報的物品: §e%s", item_DisplayName))), false);
                    Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
                }
            }
        });
    }
}
