package siongsng.rpmtwupdatemod.function;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReloadPack {
    final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    final static Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    final static String Update_Path = CACHE_DIR + "/Update.txt";
    static ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
    String Latest_ver = json.ver("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest").toString();
    String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1];

    public ReloadPack() {
        try {
            FileReader fr = new FileReader(Update_Path);
            BufferedReader br = new BufferedReader(fr);
            int Old_ver = 0;
            while (br.ready()) {
                Old_ver = Integer.parseInt(br.readLine());
            }
            fr.close();
            if (Integer.parseInt(Latest_ver_n) > Old_ver + config.Update_interval) {
                SendMsg.send("§6偵測到資源包版本過舊，正在進行更新並重新載入中...。目前版本為:" + Old_ver + "最新版本為:" + Latest_ver_n);
                FileWriter.Writer(Latest_ver_n, Update_Path); //寫入最新版本
                FileUtils.copyURLToFile(new URL(json.loadJson("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest").toString()), PACK_NAME.toFile()); //下載資源包檔案
                MinecraftClient.getInstance().reloadResources();
            } else {
                SendMsg.send("§a目前的RPMTW版本已經是最新的了!!因此不重新載入翻譯。");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SendMsg.send("§b處理完成。");
    }
}
