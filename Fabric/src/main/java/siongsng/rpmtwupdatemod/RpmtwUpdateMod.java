package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.utilities.SendMsg;
import siongsng.rpmtwupdatemod.gui.widget.RPMCheckbox;
import siongsng.rpmtwupdatemod.gui.widget.TranslucentButton;
import siongsng.rpmtwupdatemod.mixins.ChatScreenAccessor;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.utilities.Utility;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static String Mod_ID = "rpmtw_update_mod";
    public final static String PackDownloadUrl =
            Objects.equals(Locale.getDefault().getISO3Country(), "CHN") ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip" :
                    "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip";

    public RpmtwUpdateMod() {
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }
    }

    @Override
    public void onInitializeClient() {
        File CrowdinTranslationsDirectory = new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "ModTranslations");

        if (CrowdinTranslationsDirectory.exists() && CrowdinTranslationsDirectory.isDirectory()) {
            try {
                boolean deleteSuccessful = CrowdinTranslationsDirectory.delete();
                if (deleteSuccessful) {
                    LOGGER.info("刪除 ModTranslations 翻譯資料夾成功。");
                }
            } catch (Exception ignored) {

            }
        }

        key.onInitializeClient(); //註冊快捷鍵

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(Mod_ID, "rpmtw1.16");
            }

            @Override
            public void reload(ResourceManager manager) {
                if (key.updateLock) {
                    key.updateLock = false;
                    SendMsg.send("§b處理完成。");
                }
            }
        });

        LOGGER.info("Hello RPMTW world!");

        if (!RPMTWConfig.getConfig().Token.equals("")) { //如果Token不是空的
            new TokenCheck().Check(RPMTWConfig.getConfig().Token); //開始檢測
        }
        SocketClient.GetMessage();
        Handler.init();
        ScreenEvents.AFTER_INIT.register(this::screenEvent);
    }

    public void screenEvent(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        boolean showContributor = screen instanceof GameMenuScreen || screen instanceof CreativeInventoryScreen || screen instanceof InventoryScreen || screen instanceof OptionsScreen;
        List<ClickableWidget> buttons = Screens.getButtons(screen);

        // 翻譯貢獻者按鈕
        if (showContributor && RPMTWConfig.getConfig().contributorButton && RPMTWConfig.getConfig().isChinese) {
            int y = (int) (scaledHeight / 1.155);

            if (!(screen instanceof GameMenuScreen)) {
                y += 12;
            }

            TexturedButtonWidget rpmtwLogo = new TexturedButtonWidget(scaledWidth / 80, y + 2, 15, 15, 0, 0, 0, new Identifier(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            });
            ButtonWidget buttonWidget = new ButtonWidget(scaledWidth / 80 - 2, y, 20, 20, LiteralText.EMPTY, (button) -> Util.getOperatingSystem().open("https://www.rpmtw.com/Contributor"), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new LiteralText("查看 RPMTW 翻譯貢獻者")
                    , i, j));

            buttons.add(buttonWidget);
            buttons.add(rpmtwLogo);
        } else if (screen instanceof ChatScreen && RPMTWConfig.getConfig().cosmicChatButton) {

            TextFieldWidget textField = ((ChatScreenAccessor.chatFieldAccessor) (ChatScreen) screen).getChatField();

            TranslucentButton translucentButton = new TranslucentButton(scaledWidth - 185, scaledHeight - 40, 90, 20, new LiteralText("發送訊息至宇宙通訊"), (button) -> Utility.openCosmicChatScreen(textField.getText()), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new LiteralText("發送訊息至浩瀚的宇宙中，與其他星球的生物交流")
                    , i, j));

            CheckboxWidget checkbox = new RPMCheckbox(scaledWidth - 90, scaledHeight - 40, 20, 20, new LiteralText("接收宇宙通訊"), RPMTWConfig.getConfig().cosmicChat, (checked -> {
                RPMTWConfig.getConfig().cosmicChat = checked;
                RPMTWConfig.saveConfig();
            }), "接收來自其他星球的訊息");

            buttons.add(translucentButton);
            buttons.add(checkbox);
        }
    }

}