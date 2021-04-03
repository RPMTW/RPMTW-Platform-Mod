package siongsng.rpmtwupdatemod;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.gui.ConfigScreen;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ConfigScreen.class, Toml4jConfigSerializer::new); //註冊Config Gui
        key.onInitializeClient(); //註冊快捷鍵

        if (!ping.isConnect()) {
            LOGGER.error("您當前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。");
        }
        LOGGER.info("Hello RPMTW world!");
    }
}