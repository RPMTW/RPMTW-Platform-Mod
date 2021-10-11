package siongsng.rpmtwupdatemod.crowdin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.Packs.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.CosmicChatScreen;
import siongsng.rpmtwupdatemod.gui.CrowdinLoginScreen;
import siongsng.rpmtwupdatemod.gui.CrowdinProcedure;
import siongsng.rpmtwupdatemod.gui.EULAScreen;

public final class RPMKeyBinding {
    public static final KeyMapping reloadpack = new KeyMapping("key.rpmtw_update_mod.reloadpack", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyMapping open_config = new KeyMapping("key.rpmtw_update_mod.open_config", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    public static final KeyMapping Crowdin = new KeyMapping("key.rpmtw_update_mod.open_crowdin", GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyMapping cosmic_chat_send = new KeyMapping("key.rpmtw_update_mod.cosmic_chat_send", GLFW.GLFW_KEY_G, "key.categories.rpmtw");
    public static final KeyMapping translate = new KeyMapping("key.rpmtw_update_mod.translate", GLFW.GLFW_KEY_N, "key.categories.rpmtw");

    private boolean showed = false;

    public RPMKeyBinding() {
        ClientRegistry.registerKeyBinding(reloadpack);
        ClientRegistry.registerKeyBinding(open_config);
        ClientRegistry.registerKeyBinding(Crowdin);
        ClientRegistry.registerKeyBinding(cosmic_chat_send);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        Player p = Minecraft.getInstance().player;
        if (showed) { //防止重複開啟
            try {
                if (!reloadpack.isDown() && !open_config.isDown() && !Crowdin.isDown() && !cosmic_chat_send.isDown()) {
                    showed = false;
                }
            } catch (IndexOutOfBoundsException ex) {
                showed = false;
            }
            return;
        }
        if (Crowdin.consumeClick()) {
            if (!RPMTWConfig.rpmtw_crowdin.get()) return;
            assert p != null;
            Item item = p.getMainHandItem().getItem(); //拿的物品
            String item_key = item.getDescriptionId(); //物品的命名空間

            if (!RPMTWConfig.isCheck.get()) {
                Minecraft.getInstance().setScreen(new CrowdinLoginScreen());
                return;
            } else if (item_key.equals("block.minecraft.air")) {
                Minecraft client = Minecraft.getInstance();
                assert client.hitResult != null;
                HitResult.Type type = client.hitResult.getType();
                switch (type) {
                    case BLOCK -> { //指向方塊
                        BlockPos blockPos = ((BlockHitResult) client.hitResult).getBlockPos();
                        assert client.level != null;
                        item = client.level.getBlockState(blockPos).getBlock().asItem();
                        SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                        CrowdinProcedure.OpenTransactionGUI(item.getDefaultInstance());
                    }
                    case ENTITY -> { //指向實體
                        item = SpawnEggItem.byId((((EntityHitResult) client.hitResult)).getEntity().getType()); //指向實體的生怪蛋
                        assert item != null;
                        if (item.getDefaultInstance().isEmpty()) return;
                        SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                        CrowdinProcedure.OpenTransactionGUI(item.getDefaultInstance());
                    }
                    default -> SendMsg.send("§4請手持物品或十字準星對象方塊或實體後再使用此功能。");
                }
                return;
            } else if (!item_key.isEmpty()) {
                SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                CrowdinProcedure.OpenTransactionGUI(item.getDefaultInstance());
            }

        }

        if (open_config.consumeClick()) {
            Minecraft.getInstance().setScreen(new ConfigScreen());
        }
        if (reloadpack.consumeClick()) {
            if (!RPMTWConfig.rpmtw_reloadpack.get()) return;
            new ReloadPack();
        }
        if (cosmic_chat_send.consumeClick()) {
            if (!RPMTWConfig.isChat.get()) return;
            if (RPMTWConfig.isEULA.get()) {
                Minecraft.getInstance().setScreen(new CosmicChatScreen());
            } else {
                Minecraft.getInstance().setScreen(new EULAScreen());
            }
        }
    }
}
