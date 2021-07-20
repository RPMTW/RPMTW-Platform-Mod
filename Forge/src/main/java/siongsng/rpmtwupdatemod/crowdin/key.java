package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;

public final class key {
    public static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", KeyConflictContext.UNIVERSAL, Keyboard.KEY_UNLABELED, "key.categories.rpmtw");
    public static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", KeyConflictContext.UNIVERSAL, Keyboard.KEY_O, "key.categories.rpmtw");
    public static final KeyBinding Crowdin = new KeyBinding("key.rpmtw_update_mod.open_crowdin", Keyboard.KEY_UNLABELED, "key.categories.rpmtw");
    public static final KeyBinding cosmic_chat_send = new KeyBinding("key.rpmtw_update_mod.cosmic_chat_send", Keyboard.KEY_G, "key.categories.rpmtw");

    private boolean showed = false;

    public key() {
        ClientRegistry.registerKeyBinding(reloadpack);
        ClientRegistry.registerKeyBinding(open_config);
        ClientRegistry.registerKeyBinding(Crowdin);
        ClientRegistry.registerKeyBinding(cosmic_chat_send);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        if (showed) { //防止重複開啟
            try {
                if (!reloadpack.isKeyDown() && !open_config.isKeyDown() && !Crowdin.isKeyDown() && !cosmic_chat_send.isKeyDown()) {
                    showed = false;
                }
            } catch (IndexOutOfBoundsException ex) {
                showed = false;
            }
            return;
        }
        if (Crowdin.isPressed()) {
            assert p != null;
            Item item = p.getHeldItemMainhand().getItem(); //拿的物品
            String item_key = item.getTranslationKey(); //物品的命名空間

            if (item_key.equals("block.minecraft.air")) {
                SendMsg.send("§4請手持物品後再使用此功能。");
                return;
            } else if (!RPMTWConfig.isCheck) {
                Minecraft.getMinecraft().displayGuiScreen(new CrowdinLoginScreen());
                return;
            } else {
                SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                Thread thread = new Thread(() -> {
                    if (CorwidnProcedure.getText() == null && RPMTWConfig.isCheck) {
                        SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                        return;
                    }
                    Minecraft.getMinecraft().displayGuiScreen(new CrowdinScreen());
                });
                thread.start();
            }

        }

        if (open_config.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen());
        }
        if (RPMTWConfig.ReloadPack) {
            if (reloadpack.isPressed()) {
                new ReloadPack();
            }
        }
        if (cosmic_chat_send.isPressed()) {
            if (!RPMTWConfig.isChat) return;
            if (RPMTWConfig.isEULA) {
                Minecraft.getMinecraft().displayGuiScreen(new CosmicChatScreen());
            } else {
                Minecraft.getMinecraft().displayGuiScreen(new EULAScreen());
            }
        }
    }
}