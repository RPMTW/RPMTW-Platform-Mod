package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.Register.EventRegister;
import siongsng.rpmtwupdatemod.Register.RPMKeyBinding;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.gui.ping;
import siongsng.rpmtwupdatemod.gui.widget.RPMCheckbox;
import siongsng.rpmtwupdatemod.gui.widget.TranslucentButton;
import siongsng.rpmtwupdatemod.mixins.ChatScreenAccessor;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.utilities.Utility;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static String Mod_ID = "rpmtw_update_mod";
    public final static String PackDownloadUrl = Objects.equals(Locale.getDefault().getISO3Country(), "CHN")
            ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip"
            : "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip";

    public RpmtwUpdateMod() {
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }
        EventRegister.init();
    }

    @Override
    public void onInitializeClient() {
        SocketClient.init();
        new RPMKeyBinding().Register(); //註冊快捷鍵
        LOGGER.info("Hello RPMTW world!");
        if (!RPMTWConfig.getConfig().Token.equals("")) { //如果Token不是空的
            new TokenCheck().Check(RPMTWConfig.getConfig().Token); //開始檢測 Crowdin token
        }
        if (RPMTWConfig.getConfig().cosmicChat) {
            SocketClient.GetMessage();
        }
        new Handler();
        ScreenEvents.AFTER_INIT.register(this::screenEvent);
    }

    public void screenEvent(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        boolean showContributor = screen instanceof GameMenuScreen || screen instanceof CreativeInventoryScreen || screen instanceof InventoryScreen || screen instanceof OptionsScreen;
        List<ClickableWidget> buttons = Screens.getButtons(screen);

        // 翻譯貢獻者按鈕
        if (showContributor && RPMTWConfig.getConfig().contributorButton && RPMTWConfig.getConfig().isChinese) {
            int y = (int) (scaledHeight / 1.11);

            if (!(screen instanceof GameMenuScreen)) {
                y += 12;
            }

            TexturedButtonWidget rpmtwLogo = new TexturedButtonWidget(scaledWidth / 80, y + 2, 15, 15, 0, 0, 0, new Identifier(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            });
            ButtonWidget buttonWidget = new ButtonWidget(scaledWidth / 80 - 2, y, 20, 20, LiteralText.EMPTY, (button) -> Util.getOperatingSystem().open("https://www.rpmtw.com/Contributor"), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new LiteralText("查看 RPMTW 翻譯貢獻者")
                    , i, j));

            buttons.add(buttonWidget);
            buttons.add(rpmtwLogo);
        } else if (screen instanceof ChatScreen chatScreen && RPMTWConfig.getConfig().cosmicChatButton) {
            TextFieldWidget textField = ((ChatScreenAccessor.chatFieldAccessor) chatScreen).getChatField();

            TranslucentButton translucentButton = new TranslucentButton(scaledWidth - 170, scaledHeight - 40, 75, 20, new LiteralText("發送宇宙通訊"), (button) -> Utility.openCosmicChatScreen(textField.getText()), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new LiteralText("發送訊息至浩瀚的宇宙中，與其他星球的生物交流")
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