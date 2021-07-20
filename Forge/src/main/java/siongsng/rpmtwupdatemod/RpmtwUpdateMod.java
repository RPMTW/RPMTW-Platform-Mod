package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.GetMessage;
import siongsng.rpmtwupdatemod.commands.noticeCMD;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.function.AddPack;
import siongsng.rpmtwupdatemod.function.ping;
import siongsng.rpmtwupdatemod.notice.notice;

import java.io.IOException;

@Mod(
        modid = RpmtwUpdateMod.MOD_ID,
        name = RpmtwUpdateMod.MOD_NAME,
        version = RpmtwUpdateMod.VERSION,
        clientSideOnly = true,
        guiFactory = "siongsng.rpmtwupdatemod.config.ConfigScreen"
)

public class RpmtwUpdateMod {

    public static final String MOD_ID = "rpmtw_update_mod";
    public static final String MOD_NAME = "RPMTW Update Mod";
    public static final String VERSION = "1.2.1";
    public final static String PackDownloadUrl = "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.12/RPMTW-1.12.zip";

    @Mod.Instance(MOD_ID)
    public static RpmtwUpdateMod INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        LOGGER.info("Hello RPMTW world!");
        if (!ping.isConnect()) { //判斷是否有網路
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }

        new AddPack(); //新增翻譯資源包

        /*
        將語言設定為繁體中文
         */
        if (RPMTWConfig.isChinese) {
            Minecraft.getMinecraft().getLanguageManager().setCurrentLanguage(new Language("zh_tw", "TW", "繁體中文", false));
            Minecraft.getMinecraft().gameSettings.language = "zh_tw";
            Minecraft.getMinecraft().gameSettings.saveOptions();
        }
        try {
            if (!RPMTWConfig.Token.equals("")) {
                new TokenCheck().Check(RPMTWConfig.Token); //開始檢測登入權杖
            }
        } catch (IOException e) {
            LOGGER.error("檢測權杖時發生未知錯誤：" + e);
        }

        new GetMessage();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new key());
        ClientCommandHandler.instance.registerCommand(new noticeCMD());
        MinecraftForge.EVENT_BUS.register(new notice());
    }
}
