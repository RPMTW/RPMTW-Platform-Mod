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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.commands.AddToken;
import siongsng.rpmtwupdatemod.commands.noticeCMD;
import siongsng.rpmtwupdatemod.config.Config;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.discord.Chat;
import siongsng.rpmtwupdatemod.function.AFK;
import siongsng.rpmtwupdatemod.function.VersionCheck;
import siongsng.rpmtwupdatemod.notice.notice;

import java.io.IOException;
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
        //   MinecraftForge.EVENT_BUS.register(GuiHandler.class); //設定Gui註冊
        MinecraftForge.EVENT_BUS.register(new key());  //快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new AddToken()); //AddToken指令註冊
        MinecraftForge.EVENT_BUS.register(new noticeCMD()); //noticeCMD指令註冊
        if (Configer.notice.get()) { //判斷Config
            MinecraftForge.EVENT_BUS.register(new notice()); //玩家加入事件註冊
        }
        MinecraftForge.EVENT_BUS.register(new AFK()); //掛機事件註冊

        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );
    }

    public RpmtwUpdateMod() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml");
        Config.loadConfig(Config.CLIENT);
        LOGGER.info("Hello RPMTW world!");
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。");
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
        new VersionCheck(Latest_ver, Latest_ver_n, CACHE_DIR, Update_Path, PACK_NAME);
        new Chat();
    }
}