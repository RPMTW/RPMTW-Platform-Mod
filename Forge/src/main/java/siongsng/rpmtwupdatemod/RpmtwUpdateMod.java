package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.config.Config;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.gui.widget.RPMCheckbox;
import siongsng.rpmtwupdatemod.gui.widget.TranslucentButton;
import siongsng.rpmtwupdatemod.mixin.ChatScreenAccessor;
import siongsng.rpmtwupdatemod.notice.notice;
import siongsng.rpmtwupdatemod.packs.PacksManager;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.utilities.Utility;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Mod("rpmtw_update_mod")
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger(); //註冊紀錄器
    public final static String Mod_ID = "rpmtw_update_mod"; //模組ID
    public final static String PackDownloadUrl =
            Objects.equals(Locale.getDefault().getISO3Country(), "CHN") ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip" :
                    "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip";

    public RpmtwUpdateMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init); //註冊監聽事件

        LOGGER.info("Hello RPMTW world!");

        if (!ping.isConnect()) { //判斷是否有網路
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }

        Minecraft mc = Minecraft.getInstance();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml"); //註冊組態
            Config.loadConfig(Config.CLIENT);
            if (RPMTWConfig.isChinese.get()) {
                mc.gameSettings.language = "zh_tw"; //將語言設定為繁體中文
            }
        }

        PacksManager.PackVersionCheck(); //資源包版本檢查
        try {
            new TokenCheck().Check(RPMTWConfig.Token.get()); //開始檢測權杖
        } catch (IOException e) {
            LOGGER.error("檢測權杖時發生未知錯誤：" + e);
        }

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    public void init(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(new RPMKeyBinding());  //快捷鍵註冊
        if (RPMTWConfig.notice.get()) { //判斷Config
            MinecraftForge.EVENT_BUS.register(new notice()); //玩家加入事件註冊
        }
        ConfigScreen.registerConfigScreen();

        SocketClient.GetMessage();
        MinecraftForge.EVENT_BUS.register(new Handler());
        MinecraftForge.EVENT_BUS.addListener(this::screenEvent);
    }

    public void screenEvent(GuiScreenEvent.InitGuiEvent event) {
        Screen screen = event.getGui();
        int scaledWidth = screen.width;
        int scaledHeight = screen.height;

        boolean showContributor = screen instanceof IngameMenuScreen || screen instanceof CreativeScreen || screen instanceof InventoryScreen || screen instanceof OptionsScreen;
        // 翻譯貢獻者按鈕
        if (showContributor && RPMTWConfig.contributorButton.get() && RPMTWConfig.isChinese.get()) {
            int y = (int) (scaledHeight / 1.155);

            if (!(screen instanceof IngameMenuScreen)) {
                y += 10;
            }

            ImageButton rpmtwLogo = new ImageButton(scaledWidth / 80, y + 2, 15, 15, 0, 0, 0, new ResourceLocation(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            });
            Button buttonWidget = new Button(scaledWidth / 80 - 2, y, 20, 20, StringTextComponent.EMPTY, (button) -> Util.getOSType().openURI("https://www.rpmtw.com/Contributor"), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new StringTextComponent("查看 RPMTW 翻譯貢獻者")
                    , i, j));

            event.addWidget(buttonWidget);
            event.addWidget(rpmtwLogo);
        } else if (screen instanceof ChatScreen  && RPMTWConfig.cosmicChatButton.get()) {
            TextFieldWidget textField = ((ChatScreenAccessor.chatFieldAccessor) screen).getChatField();

            TranslucentButton translucentButton = new TranslucentButton(scaledWidth - 185, scaledHeight - 40, 90, 20, new StringTextComponent("發送訊息至宇宙通訊"), (button) -> Utility.openCosmicChatScreen(textField.getText()), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new StringTextComponent("發送訊息至浩瀚的宇宙中，與其他星球的生物交流")
                    , i, j));

            RPMCheckbox checkbox = new RPMCheckbox(scaledWidth - 90, scaledHeight - 40, 20, 20, new StringTextComponent("接收宇宙通訊"), RPMTWConfig.cosmicChat.get(), (checked -> RPMTWConfig.cosmicChat.set(checked)), "接收來自其他星球的訊息");

            event.addWidget(translucentButton);
            event.addWidget(checkbox);
        }
    }

}