package siongsng.rpmtwupdatemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RpmtwUpdateMod.MOD_ID,
        name = RpmtwUpdateMod.MOD_NAME,
        version = RpmtwUpdateMod.VERSION,
        clientSideOnly = true
)

public class RpmtwUpdateMod {

    public static final String MOD_ID = "rpmtw_update_mod";
    public static final String MOD_NAME = "RPMTW Update Mod";
    public static final String VERSION = "1.2.1";
    public final static String PackDownloadUrl = "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.12/RPMTW-1.12.zip";

    @Mod.Instance(MOD_ID)
    public static RpmtwUpdateMod INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public RpmtwUpdateMod() {
//        MinecraftForge.EVENT_BUS.register(this);
//        LOGGER.info("Hello RPMTW world!");
//        if (!ping.isConnect()) { //判斷是否有網路
//            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
//        }
//        new PackVersionCheck(); //資源包版本檢查
//        Minecraft.getMinecraft().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
//        try {
//            new TokenCheck().Check(Configer.Token); //開始檢測權杖
//        } catch (IOException e) {
//            LOGGER.error("檢測權杖時發生未知錯誤：" + e);
//        }
    }

//    public static void insertForcedPack(List resourcePackList) {
//
//    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }
}
