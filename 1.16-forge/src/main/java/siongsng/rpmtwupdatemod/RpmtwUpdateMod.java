package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.resources.FilePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
import java.util.function.Consumer;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    public final static Path LANGUAGE_PACK = CACHE_DIR.resolve("RPMTW-1.16.zip");
    public final static String LINK = json.loadJson().toString();
    public final static long MAX_INTERVAL_DAYS = 7;

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

        if (Files.exists(LANGUAGE_PACK)) {
            try {
                long fileTime = Files.getLastModifiedTime(LANGUAGE_PACK).toMillis();
                long nowTime = System.currentTimeMillis();
                if (TimeUnit.MILLISECONDS.toDays(nowTime - fileTime) < MAX_INTERVAL_DAYS) {
                    Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.copyURLToFile(new URL(LINK), LANGUAGE_PACK.toFile());
                Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onClientStarting(FMLClientSetupEvent event) {
        Minecraft.getInstance().getLanguageManager().setCurrentLanguage(new Language("zh_tw", "TW", "繁體中文", false));
        String path = System.getProperty("user.home") + "/.rpmtw/1.16";
        try {
            FileDownloadManager g = new FileDownloadManager(json.loadJson().toString(), "rpmtw-1.16.zip", path);
            g.start("go");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}