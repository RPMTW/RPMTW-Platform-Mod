package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.PackFinder;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.json;

import java.io.IOException;
import java.net.URL;

public final class key {
    public static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.rpmtw");
    public static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.rpmtw");
    public static final KeyBinding report_translation = new KeyBinding("key.rpmtw_update_mod.report_translation", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.categories.rpmtw");
    public static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    private boolean showed = false;

    public key() {
        ClientRegistry.registerKeyBinding(crowdin);
        ClientRegistry.registerKeyBinding(reloadpack);
        ClientRegistry.registerKeyBinding(report_translation);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        PlayerEntity p = Minecraft.getInstance().player;
        if (showed) { //防止重複開啟
            try {
                if (!crowdin.isKeyDown() && !reloadpack.isKeyDown() && !report_translation.isKeyDown() && !open_config.isKeyDown()) {
                    showed = false;
                }
            } catch (IndexOutOfBoundsException ex) {
                showed = false;
            }
            return;
        }
        if (open_config.isPressed()) {
            Minecraft.getInstance().displayGuiScreen(new ConfigScreen());
        }
        if (Configer.rpmtw_crowdin.get()) {
            if (crowdin.isPressed()) {
                assert p != null;
                Item item = p.getHeldItemMainhand().getItem(); //拿的物品

                String mod_id = item.getCreatorModId(p.getHeldItemMainhand().getStack()); //物品所屬的模組ID
                String item_key = item.getTranslationKey(); //物品的命名空間
                String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                if (item_key.equals("block.minecraft.air")) {
                    p.sendMessage(new StringTextComponent("§4請手持物品後再使用此功能。"), p.getUniqueID()); //發送訊息
                    return;
                }
                String msg = String.format(
                        "§c-------------------------\n" +
                                "§b模組ID: §a%s\n" +
                                "§b顯示名稱: §a%s\n" +
                                "§b命名空間: §a%s\n" +
                                "§c-------------------------", mod_id, item_DisplayName, item_key);
                p.sendMessage(new StringTextComponent(msg), p.getUniqueID()); //發送訊息

                String url = "https://translate.rpmtw.ga/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + item_key;
                p.sendMessage(new StringTextComponent("§6開啟翻譯平台網頁中..."), p.getUniqueID()); //發送訊息
                Util.getOSType().openURI(url); //使用預設瀏覽器開啟網頁
            }
        }
        if (Configer.rpmtw_reloadpack.get()) {
            if (reloadpack.isPressed()) {
                try {
                    FileUtils.copyURLToFile(new URL(json.loadJson().toString()), RpmtwUpdateMod.PACK_NAME.toFile()); //下載資源包檔案
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
                Minecraft.getInstance().getResourcePackList().reloadPacksFromFinders();
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("§6重新下載最新資源包並且載入RPMTW繁體中文化資源包完畢"), Minecraft.getInstance().player.getUniqueID());
            }
        }
        if (Configer.report_translation.get()) {
            if (report_translation.isPressed()) {
                assert p != null;
                Item item = p.getHeldItemMainhand().getItem(); //拿的物品

                String mod_id = item.getCreatorModId(p.getHeldItemMainhand().getStack()); //物品所屬的模組ID
                String item_key = item.getTranslationKey(); //物品的命名空間
                String item_DisplayName = item.getName().getString(); //物品的顯示名稱
                if (item_key.equals("block.minecraft.air")) {
                    p.sendMessage(new StringTextComponent("§4請手持要回報翻譯錯誤的物品或方塊..."), p.getUniqueID()); //發送訊息
                    return;
                }
                String url = String.format("https://docs.google.com/forms/d/e/1FAIpQLSelkP16fMms-_3q4ewdVLaDO14YdmmupcZ2Yl1V0sPtuC-v_g/viewform?usp=pp_url&entry.412976727=%s&entry.2706446=%s", mod_id, item_key);
                p.sendMessage(new StringTextComponent(String.format("§6即將開啟回報錯誤的網頁中...\n回報的物品: §e%s", item_DisplayName)), p.getUniqueID()); //發送訊息
                Util.getOSType().openURI(url); //使用預設瀏覽器開啟網頁
            }
        }
    }
}
