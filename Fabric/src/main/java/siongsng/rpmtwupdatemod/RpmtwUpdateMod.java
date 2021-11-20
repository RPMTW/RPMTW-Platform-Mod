package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.Register.EventRegister;
import siongsng.rpmtwupdatemod.Register.RPMKeyBinding;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.gui.ping;
import siongsng.rpmtwupdatemod.translation.Handler;

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
            new TokenCheck().Check(RPMTWConfig.getConfig().Token); //開始檢測
        }
        if (RPMTWConfig.getConfig().isChat) {
            SocketClient.GetMessage();
        }
        Handler.init();
    }

}