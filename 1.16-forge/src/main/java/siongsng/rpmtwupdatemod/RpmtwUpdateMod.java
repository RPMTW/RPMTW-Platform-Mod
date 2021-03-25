package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    public final static Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    public final static long MAX_Hours = 1;

    public RpmtwUpdateMod() {
        MinecraftForge.EVENT_BUS.register(this);
        if (!ping.isConnect()) {
            LOGGER.info("您當前處於無網路狀態\n因此無法使用RPMTW自動更新模組\n請連結網路後重新啟動此模組。");
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
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
                if (TimeUnit.MILLISECONDS.toHours(nowTime - fileTime) < MAX_Hours) {
                    Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.copyURLToFile(new URL(json.loadJson().toString()), PACK_NAME.toFile());
                Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}