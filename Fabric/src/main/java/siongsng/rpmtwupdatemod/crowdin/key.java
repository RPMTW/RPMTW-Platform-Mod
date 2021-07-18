package siongsng.rpmtwupdatemod.crowdin;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.*;

public class key {
    private static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    private static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    private static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    private static final KeyBinding cosmic_chat_send = new KeyBinding("key.rpmtw_update_mod.cosmic_chat_send", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.rpmtw");

    static ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();

    public static void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);
        KeyBindingHelper.registerKeyBinding(open_config);
        if (Configer.config.isChat) {
            KeyBindingHelper.registerKeyBinding(cosmic_chat_send);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (cosmic_chat_send.wasPressed()) {
                if (Configer.config.isEULA) {
                    MinecraftClient.getInstance().openScreen(new Screen(new CosmicChat()));
                } else {
                    MinecraftClient.getInstance().openScreen(new Screen(new EULA()));
                }
            }
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
                        MinecraftClient.getInstance().openScreen(new Screen(new CrowdinLogin()));
                        return;
                    } else {
                        SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                        Thread thread = new Thread(() -> {
                            if (CrowdinGuiProcedure.getText() == null && config.isCheck) {
                                SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                                return;
                            }
                            MinecraftClient.getInstance().openScreen(new Screen(new CrowdinGui()));
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
        });
    }
}
