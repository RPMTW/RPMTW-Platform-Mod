package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");

    @Override
    public void onInitializeClient() {
        if (!ping.isConnect()) {
            LOGGER.info("您當前處於無網路狀態\n因此無法使用RPMTW自動更新模組\n請連結網路後重新啟動此模組。");
        }
        LOGGER.info("Hello RPMTW world!");
        LOGGER.info("正在準備進行更新資源包，最新版本:" + json.ver().toString());
    }
}