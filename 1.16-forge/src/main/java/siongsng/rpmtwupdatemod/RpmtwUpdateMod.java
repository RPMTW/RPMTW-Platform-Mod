package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger();

    public RpmtwUpdateMod() {
        MinecraftForge.EVENT_BUS.register(this);
        if (!ping.isConnect()) {
            JOptionPane.showMessageDialog(null, "test", "test",
                    JOptionPane.QUESTION_MESSAGE);
        }

        if (FMLEnvironment.dist == Dist.CLIENT) {

            Minecraft.getInstance().getResourcePackList().addPackFinder(PackFinder.RESOUCE);
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
        LOGGER.info("正在準備進行更新資源包，最新版本:" + json.ver().toString());
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
//您當前處於無網路狀態
//因此無法使用RPMTW自動更新模組
//請連結網路後重新啟動此模組。