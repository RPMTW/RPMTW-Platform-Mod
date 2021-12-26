package siongsng.rpmtwupdatemod;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.Packs.PackVersionCheck;
import siongsng.rpmtwupdatemod.config.Config;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.onPlayerJoin;
import siongsng.rpmtwupdatemod.function.ping;
import siongsng.rpmtwupdatemod.gui.widget.RPMCheckbox;
import siongsng.rpmtwupdatemod.gui.widget.TranslucentButton;
import siongsng.rpmtwupdatemod.mixin.ChatScreenAccessor;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.utilities.Utility;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Mod("rpmtw_update_mod")
public class RpmtwUpdateMod {
    public final static String Mod_ID = "rpmtw_update_mod"; // 模組ID
    public static final Logger LOGGER = LogManager.getLogger(Mod_ID); // 註冊紀錄器
    public final static String PackDownloadUrl = Objects.equals(Locale.getDefault().getISO3Country(), "CHN")
            ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip"
            : "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip";

    public RpmtwUpdateMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init); // 註冊初始化事件
        FMLJavaModLoadingContext.get().getModEventBus().addListener(new Handler()::registerReload); // 註冊資源重新載入事件

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml"); // 註冊組態

        if (FMLEnvironment.dist == Dist.CLIENT) {
            Config.loadConfig(Config.CLIENT); // 載入客戶端組態

            LOGGER.info("Hello RPMTW world!");
            if (!ping.isConnect()) { // 判斷是否有網路
                LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
            }
            if (RPMTWConfig.isChinese.get()) {
                Minecraft.getInstance().options.languageCode = "zh_tw"; // 將語言設定為繁體中文
            }
            PackVersionCheck.check(); // 資源包版本檢查
            try {
                new TokenCheck().Check(RPMTWConfig.Token.get()); // 開始檢測權杖
            } catch (IOException e) {
                LOGGER.error("檢測權杖時發生未知錯誤：" + e);
            }
        }
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,
                        (remote, isServer) -> true));

    }

    public void init(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(new RPMKeyBinding()); // 快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new onPlayerJoin()); // 玩家加入事件註冊
        ConfigScreen.registerConfigScreen();

        boolean isDebug = Boolean.parseBoolean(System.getProperty("siongsng.rpmtwupdatemod.RpmtwUpdateMod.isDebug"));

        if (!isDebug) {
            SocketClient.GetMessage();
        }

        MinecraftForge.EVENT_BUS.register(new Handler());
        MinecraftForge.EVENT_BUS.addListener(this::screenEvent);
    }

    public void screenEvent(ScreenEvent.InitScreenEvent event) {
        Screen screen = event.getScreen();
        int scaledWidth = screen.width;
        int scaledHeight = screen.height;

        boolean showContributor = screen instanceof PauseScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof InventoryScreen || screen instanceof OptionsScreen;
        // 翻譯貢獻者按鈕
        if (showContributor && RPMTWConfig.contributorButton.get() && RPMTWConfig.isChinese.get()) {
            int y = (int) (scaledHeight / 1.155);

            if (screen instanceof OptionsScreen) {
                y += 12;
            } else if (screen instanceof CreativeModeInventoryScreen || screen instanceof InventoryScreen) {
                y -= 20;
            }

            ImageButton rpmtwLogo = new ImageButton(scaledWidth / 80, y + 2, 15, 15, 0, 0, 0, new ResourceLocation(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            });
            Button buttonWidget = new Button(scaledWidth / 80 - 2, y, 20, 20, TextComponent.EMPTY, (button) -> Util.getPlatform().openUri("https://www.rpmtw.com/Contributor"), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new TextComponent("查看 RPMTW 翻譯貢獻者")
                    , i, j));

            event.addListener(buttonWidget);
            event.addListener(rpmtwLogo);
        } else if (screen instanceof ChatScreen chatScreen && RPMTWConfig.cosmicChatButton.get()) {
            EditBox textField = ((ChatScreenAccessor.chatFieldAccessor) chatScreen).getChatField();

            TranslucentButton translucentButton = new TranslucentButton(scaledWidth - 185, scaledHeight - 40, 90, 20, new TextComponent("發送訊息至宇宙通訊"), (button) -> Utility.openCosmicChatScreen(textField.getValue()), (buttonWidget1, matrixStack, i, j) -> screen.renderTooltip(matrixStack, new TextComponent("發送訊息至浩瀚的宇宙中，與其他星球的生物交流")
                    , i, j));

            RPMCheckbox checkbox = new RPMCheckbox(scaledWidth - 90, scaledHeight - 40, 20, 20, new TextComponent("接收宇宙通訊"), RPMTWConfig.cosmicChat.get(), (checked -> RPMTWConfig.cosmicChat.set(checked)), "接收來自其他星球的訊息");

            event.addListener(translucentButton);
            event.addListener(checkbox);
        }
    }
}