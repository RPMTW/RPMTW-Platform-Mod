package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.crowdin.key;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static String Mod_ID = "rpmtw_update_mod";

    @Override
    public void onInitializeClient() {
        key.onInitializeClient(); //註冊快捷鍵

        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。");
        }
        LOGGER.info("Hello RPMTW world!");
    }
}