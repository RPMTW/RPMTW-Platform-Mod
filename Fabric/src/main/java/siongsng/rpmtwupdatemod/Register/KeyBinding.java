package siongsng.rpmtwupdatemod.Register;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.packs.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.CosmicChat;
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
            while (crowdin.wasPressed()) { //開啟物品翻譯界面
                if (!RPMTWConfig.config.crowdin) return;
                assert client.player != null;
                Item item = client.player.getMainHandStack().getItem(); //取得手上拿的物品
                String item_key = item.getTranslationKey(); //物品的命名空間

                if (!RPMTWConfig.config.isCheck) {
                    MinecraftClient.getInstance().openScreen(new Screen(new CrowdinLogin()));
                    return;
                } else if (item_key.equals("block.minecraft.air")) {
                    assert client.crosshairTarget != null;
                    HitResult.Type type = client.crosshairTarget.getType();
                    switch (type) {
                        case BLOCK -> { //指向方塊
                            BlockPos blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
                            assert client.world != null;
                            item = client.world.getBlockState(blockPos).getBlock().asItem();
                            CrowdinGuiProcedure.OpenTransactionGUI(item.getDefaultStack());
                        }
                        case ENTITY -> { //指向實體
                            item = SpawnEggItem.forEntity(((EntityHitResult) client.crosshairTarget).getEntity().getType()); //指向實體的生怪蛋
                            assert item != null;
                            if (item.getDefaultStack().isEmpty()) return;
                            CrowdinGuiProcedure.OpenTransactionGUI(item.getDefaultStack());
                        }
                        default -> SendMsg.send("§4請手持物品或十字準星對象方塊或實體後再使用此功能。");
                    }
                    return;
                } else if (!item_key.isEmpty()) {
                    CrowdinGuiProcedure.OpenTransactionGUI(item.getDefaultStack());
                }
            }
            while (reloadpack.wasPressed()) { //更新翻譯包
                if (!RPMTWConfig.config.ReloadPack) return;
                new ReloadPack();
            }

        });
    }
}
