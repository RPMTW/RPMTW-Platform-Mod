package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.commands.AddToken;
import siongsng.rpmtwupdatemod.config.Config;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.function.File_Writer;
import siongsng.rpmtwupdatemod.gui.GuiHandler;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public final static String Mod_ID = "rpmtw_update_mod";
    public final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    public final static Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    public final static String Update_Path = CACHE_DIR + "/Update.txt";
    public final static String Latest_ver = json.ver().toString();
    public final static String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1];


    @SubscribeEvent
    public void init(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(GuiHandler.class); //設定Gui註冊
        MinecraftForge.EVENT_BUS.register(new key()); //快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new AddToken()); //指令註冊

        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml");
        Config.loadConfig(Config.CLIENT, FMLPaths.CONFIGDIR.get().resolve("rpmtw_update_mod-client.toml").toString());
    }

    public RpmtwUpdateMod() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        LOGGER.info("Hello RPMTW world!");
        LOGGER.info(Integer.parseInt(Latest_ver_n));
        if (!ping.isConnect()) {
            LOGGER.error("您當前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。(此偵測僅對Windows作業系統有效)");
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
        LOGGER.info("正在準備檢測資源包版本，最新版本:" + Latest_ver);
        if (!Files.isDirectory(CACHE_DIR)) { //如果沒有資源包路徑
            Files.createDirectories(CACHE_DIR);
        }
        if (!Files.exists(Paths.get(CACHE_DIR + "/Update.txt"))) { //如果沒有更新檔案
            Files.createFile(Paths.get(CACHE_DIR + "/Update.txt")); //建立更新檔案
            File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
        }
        if (Files.exists(PACK_NAME) || !Files.exists(Paths.get(CACHE_DIR + "/RPMTW-1.16.zip"))) { //如果有資源包檔案
            FileReader fr = new FileReader(Update_Path);
            BufferedReader br = new BufferedReader(fr);
            int Old_ver = 0;
            while (br.ready()) {
                Old_ver = Integer.parseInt(br.readLine());
                System.out.println(br.readLine());
            }
            fr.close();
            if (Integer.parseInt(Latest_ver_n) > Old_ver || !Files.exists(Paths.get(CACHE_DIR + "/RPMTW-1.16.zip"))) {
                LOGGER.info("偵測到資源包版本過舊，正在進行更新中...\n最新版本為" + Latest_ver_n);
                File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
                FileUtils.copyURLToFile(new URL(json.loadJson().toString()), PACK_NAME.toFile()); //下載資源包檔案
            } else {
                LOGGER.info("您目前的版本已經是最新的了!!");
            }
            Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
        }
    }
}