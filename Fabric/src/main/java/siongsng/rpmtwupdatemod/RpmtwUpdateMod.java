package siongsng.rpmtwupdatemod;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.GetMessage;
import siongsng.rpmtwupdatemod.Register.KeyBinding;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.gui.ping;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static String Mod_ID = "rpmtw_update_mod";
    public final static String PackDownloadUrl = "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.17/RPMTW-1.17.zip";

    public RpmtwUpdateMod() {
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }
        AutoConfig.register(ConfigScreen.class, Toml4jConfigSerializer::new); //註冊Config
    }

    @Override
    public void onInitializeClient() {
        new KeyBinding().Register(); //註冊快捷鍵
        LOGGER.info("Hello RPMTW world!");
        if (!RPMTWConfig.config.Token.equals("")) { //如果Token不是空的
            new TokenCheck().Check(RPMTWConfig.config.Token); //開始檢測
        }
        if (RPMTWConfig.config.isChat) {
            new GetMessage();
        }
    }

}