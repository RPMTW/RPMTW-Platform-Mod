package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.*;

public final class key {
    public static final KeyBinding reloadpack = new KeyBinding("重新載入翻譯包", KeyConflictContext.UNIVERSAL, Keyboard.KEY_NONE, "RPMTW 快捷鍵");
    public static final KeyBinding open_config = new KeyBinding("開啟RPMTW設定選單", KeyConflictContext.UNIVERSAL, Keyboard.KEY_O, "RPMTW 快捷鍵");
    public static final KeyBinding Crowdin = new KeyBinding("開啟物品翻譯界面", Keyboard.KEY_NONE, "RPMTW 快捷鍵");
    public static final KeyBinding cosmic_chat_send = new KeyBinding("開啟宇宙通訊發送介面", Keyboard.KEY_G, "RPMTW 快捷鍵");

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

            if (item == Items.AIR || p.getHeldItemMainhand() == ItemStack.EMPTY) {
                SendMsg.send("§4請手持物品後再使用此功能。");
                return;
            } else if (!RPMTWConfig.isCheck) {
                Minecraft.getMinecraft().displayGuiScreen(new CrowdinLoginScreen());
                return;
            } else {
                SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                Thread thread = new Thread(() -> {
                    if (CrowdinProcedure.getText() == null && RPMTWConfig.isCheck) {
                        SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                        return;
                    }
                    Minecraft.getMinecraft().displayGuiScreen(new CrowdinScreen());
                });
                thread.start();
            }
        }

        if (open_config.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(null));
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