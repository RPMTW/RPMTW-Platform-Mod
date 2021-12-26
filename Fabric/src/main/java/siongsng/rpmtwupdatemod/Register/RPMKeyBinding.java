package siongsng.rpmtwupdatemod.Register;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.gui.CrowdinGui.CrowdinInfo;
import siongsng.rpmtwupdatemod.gui.CrowdinLogin.CrowdinLogin;
import siongsng.rpmtwupdatemod.gui.Screen;
import siongsng.rpmtwupdatemod.packs.PackManeger;
import siongsng.rpmtwupdatemod.utilities.SendMsg;
import siongsng.rpmtwupdatemod.utilities.Utility;

public class RPMKeyBinding {
    public static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    public static final KeyBinding cosmic_chat_send = new KeyBinding("key.rpmtw_update_mod.cosmic_chat_send", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.rpmtw");
    public static final KeyBinding translate = new KeyBinding("key.rpmtw_update_mod.translate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.rpmtw");


    public static boolean updateLock = false;

    public void Register() {
        KeyBindingHelper.registerKeyBinding(crowdin);
        KeyBindingHelper.registerKeyBinding(reloadpack);
        KeyBindingHelper.registerKeyBinding(open_config);
        KeyBindingHelper.registerKeyBinding(cosmic_chat_send);
        KeyBindingHelper.registerKeyBinding(translate);
        Events();
    }

    public void Events() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (cosmic_chat_send.wasPressed()) { //開啟宇宙通訊介面
                if (!RPMTWConfig.getConfig().cosmicChat) return;
                Utility.openCosmicChatScreen("");
            }
            while (open_config.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setScreen(AutoConfig.getConfigScreen(ConfigScreen.class, mc.currentScreen).get());
            }
            while (crowdin.wasPressed()) { //開啟物品翻譯界面
                if (!RPMTWConfig.getConfig().crowdin) return;
                PlayerEntity player =  client.player;
                World world = client.world;
               if (player != null && world != null) {
                   Item item =player.getMainHandStack().getItem(); //取得手上拿的物品
                   String item_key = item.getTranslationKey(); //物品的命名空間

                   if (!RPMTWConfig.getConfig().isCheck) {
                       MinecraftClient.getInstance().setScreen(new Screen(new CrowdinLogin()));
                       return;
                   } else if (item_key.equals("block.minecraft.air")) {
                       assert client.crosshairTarget != null;
                       HitResult.Type type = client.crosshairTarget.getType();
                       switch (type) {
                           case BLOCK -> { //指向方塊
                               BlockPos blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
                               item = world.getBlockState(blockPos).getBlock().asItem();
                               CrowdinInfo.openTransactionGUI(item.getDefaultStack());
                           }
                           case ENTITY -> { //指向實體
                               item = SpawnEggItem.forEntity(((EntityHitResult) client.crosshairTarget).getEntity().getType()); //指向實體的生怪蛋
                               assert item != null;
                               if (item.getDefaultStack().isEmpty()) return;
                               CrowdinInfo.openTransactionGUI(item.getDefaultStack());
                           }
                           default -> SendMsg.send("§4請手持物品或十字準星對象方塊或實體後再使用此功能。");
                       }
                       return;
                   } else if (!item_key.isEmpty()) {
                       CrowdinInfo.openTransactionGUI(item.getDefaultStack());
                   }
               }
            }
            while (reloadpack.wasPressed() && !updateLock) { //更新翻譯包
                if (!RPMTWConfig.getConfig().ReloadPack)
                    return;

                updateLock = true;
                PackManeger.ReloadPack();
            }

        });
    }
}
