package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
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
import siongsng.rpmtwupdatemod.translation.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

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
        if (RPMTWConfig.getConfig().isChat) {
            SocketClient.GetMessage();
        }
        new Handler();
        ScreenEvents.AFTER_INIT.register(this::screenEvent);
    }

    public void screenEvent(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        boolean show = screen instanceof GameMenuScreen || screen instanceof CreativeInventoryScreen || screen instanceof InventoryScreen || screen instanceof OptionsScreen;

        if (show && RPMTWConfig.getConfig().contributorButton && RPMTWConfig.getConfig().isChinese) {


            TexturedButtonWidget rpmtwLogo = new TexturedButtonWidget(scaledWidth / 80, scaledHeight - 22, 15, 15, 0, 0, 0, new Identifier(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            }, new LiteralText(""));
            ButtonWidget buttonWidget = new ButtonWidget(scaledWidth / 80 - 2, scaledHeight - 25, 20, 20, LiteralText.EMPTY, (button) -> Util.getOperatingSystem().open("https://www.rpmtw.com/Contributor"), new ButtonWidget.TooltipSupplier() {
                public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrixStack, int i, int j) {
                    this.supply(consumer -> {
                        List<OrderedText> lines = new ArrayList<>();
                        lines.add(consumer.asOrderedText());
                        screen.renderOrderedTooltip(matrixStack, lines
                                , i, j);
                    });
                }

                public void supply(Consumer<Text> consumer) {
                    consumer.accept(new LiteralText("查看 RPMTW 翻譯貢獻者"));
                }
            });

            List<ClickableWidget> buttons = Screens.getButtons(screen);
            buttons.add(buttonWidget);
            buttons.add(rpmtwLogo);
        }
    }

}