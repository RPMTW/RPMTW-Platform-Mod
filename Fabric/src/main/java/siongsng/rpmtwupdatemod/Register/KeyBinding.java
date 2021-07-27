package siongsng.rpmtwupdatemod.Register;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.CosmicChat;
import siongsng.rpmtwupdatemod.gui.CrowdinGui.CrowdinGui;
import siongsng.rpmtwupdatemod.gui.CrowdinGui.CrowdinGuiProcedure;
import siongsng.rpmtwupdatemod.gui.CrowdinLogin.CrowdinLogin;
import siongsng.rpmtwupdatemod.gui.EULA;
import siongsng.rpmtwupdatemod.gui.Screen;

public class KeyBinding {
    public static final net.minecraft.client.option.KeyBinding crowdin = new net.minecraft.client.option.KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final net.minecraft.client.option.KeyBinding reloadpack = new net.minecraft.client.option.KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final net.minecraft.client.option.KeyBinding open_config = new net.minecraft.client.option.KeyBinding("key.rpmtw_update_mod.open_config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    public static final net.minecraft.client.option.KeyBinding cosmic_chat_send = new net.minecraft.client.option.KeyBinding("key.rpmtw_update_mod.cosmic_chat_send", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.rpmtw");

    public void Register() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);
        KeyBindingHelper.registerKeyBinding(open_config);
        KeyBindingHelper.registerKeyBinding(cosmic_chat_send);
        Events();
    }

    public void Events() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (cosmic_chat_send.wasPressed()) { //開啟宇宙通訊介面
                if (!RPMTWConfig.config.isChat) return;
                if (RPMTWConfig.config.isEULA) {
                    MinecraftClient.getInstance().openScreen(new Screen(new CosmicChat()));
                } else {
                    MinecraftClient.getInstance().openScreen(new Screen(new EULA()));
                }
            }
            while (open_config.wasPressed()) {
                MinecraftClient.getInstance().openScreen(AutoConfig.getConfigScreen(ConfigScreen.class, MinecraftClient.getInstance().currentScreen).get());
            }
            while (crowdin.wasPressed()) { //物品翻譯界面
                if (!RPMTWConfig.config.crowdin) return;
                assert client.player != null;
                Item item = client.player.getMainHandStack().getItem();
                String item_key = item.getTranslationKey(); //物品的命名空間

                if (item_key.equals("block.minecraft.air")) {
                    SendMsg.send("§4請手持物品後再使用此功能。");
                    return;
                } else if (!RPMTWConfig.config.isCheck) {
                    MinecraftClient.getInstance().openScreen(new Screen(new CrowdinLogin()));
                    return;
                } else {
                    SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                    if (CrowdinGuiProcedure.getText(item.getTranslationKey()) == null && RPMTWConfig.config.isCheck) {
                        SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                        return;
                    }
                    CrowdinGuiProcedure.SetItemStack(item.getDefaultStack());
                    MinecraftClient.getInstance().openScreen(new Screen(new CrowdinGui()));
                }
            }
            while (reloadpack.wasPressed()) { //更新翻譯包
                if (!RPMTWConfig.config.ReloadPack) return;
                new ReloadPack();
            }

        });
    }
}
