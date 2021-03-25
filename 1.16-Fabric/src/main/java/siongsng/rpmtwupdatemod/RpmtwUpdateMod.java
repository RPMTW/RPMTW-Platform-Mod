package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    public final static Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    public final static long MAX_Hours = 1;

    @Override
    public void onInitializeClient() {
        if (!ping.isConnect()) {
            LOGGER.info("您當前處於無網路狀態\n因此無法使用RPMTW自動更新模組\n請連結網路後重新啟動此模組。");
        }
        LOGGER.info("Hello RPMTW world!");
        LOGGER.info("正在準備進行更新資源包，最新版本:" + json.ver().toString());
        if (!Files.isDirectory(CACHE_DIR)) {
            try {
                Files.createDirectories(CACHE_DIR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Files.exists(PACK_NAME)) {
            try {
                long fileTime = Files.getLastModifiedTime(PACK_NAME).toMillis();
                long nowTime = System.currentTimeMillis();
                if (TimeUnit.MILLISECONDS.toHours(nowTime - fileTime) > MAX_Hours) {
                    try {
                        FileUtils.copyURLToFile(new URL(json.loadJson().toString()), PACK_NAME.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}