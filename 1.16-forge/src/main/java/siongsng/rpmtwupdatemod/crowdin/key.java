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
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.crowdinConfig;
import siongsng.rpmtwupdatemod.config.reloadpackConfig;


public final class key {
    private static final KeyBinding crowdin = new KeyBinding("key.rpmtw_update_mod.crowdin", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.rpmtw");
    private static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.rpmtw");
    private boolean showed = false;

    public key() {
        ClientRegistry.registerKeyBinding(crowdin);
        ClientRegistry.registerKeyBinding(reloadpack);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        PlayerEntity p = Minecraft.getInstance().player;
        if (showed) { //防止重複開啟
            try {
                if (!crowdin.isKeyDown() && !reloadpack.isKeyDown()) {
                    showed = false;
                }
            } catch (IndexOutOfBoundsException ex) {
                showed = false;
            }
            return;
        }
        if (crowdinConfig.rpmtw_crowdin.get()) {
            if (crowdin.isPressed()) { //手拿著物品右鍵點擊物品又同時點擊快捷鍵
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
        if (reloadpackConfig.rpmtw_reloadpack.get()) {
            if (reloadpack.isPressed()) {
                Minecraft.getInstance().getLanguageManager().onResourceManagerReload(Minecraft.getInstance().getResourceManager());
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("§6重新載入RPMTW繁體中文化資源包完畢"), Minecraft.getInstance().player.getUniqueID());
            }
        }
    }
}
