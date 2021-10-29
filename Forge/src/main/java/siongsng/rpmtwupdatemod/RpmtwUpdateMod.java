package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;
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
import siongsng.rpmtwupdatemod.translation.Handler;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Mod("rpmtw_update_mod")
public class RpmtwUpdateMod {
    public final static String Mod_ID = "rpmtw_update_mod"; // 模組ID
    public static final Logger LOGGER = LogManager.getLogger(); // 註冊紀錄器
    public final static String PackDownloadUrl = Objects.equals(Locale.getDefault().getISO3Country(), "CHN")
            ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.17/RPMTW-1.17.zip"
            : "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.17/RPMTW-1.17.zip";

    public RpmtwUpdateMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init); // 註冊初始化事件

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
            new PackVersionCheck(); // 資源包版本檢查
            try {
                new TokenCheck().Check(RPMTWConfig.Token.get()); // 開始檢測權杖
            } catch (IOException e) {
                LOGGER.error("檢測權杖時發生未知錯誤：" + e);
            }
        }
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY,
                        (remote, isServer) -> true));

    }

    public void init(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(new RPMKeyBinding()); // 快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new onPlayerJoin()); // 玩家加入事件註冊
        ConfigScreen.registerConfigScreen();

        boolean isDebug = Boolean.parseBoolean(System.getProperty("siongsng.rpmtwupdatemod.RpmtwUpdateMod.isDebug"));

        if (RPMTWConfig.isChat.get() && !isDebug) {
            SocketClient.GetMessage();
        }
        MinecraftForge.EVENT_BUS.register(new Handler());
    }
}