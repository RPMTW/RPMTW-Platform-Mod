package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.PackFinder;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackVersionCheck {
    public PackVersionCheck(String Latest_ver, String Latest_ver_n, Path CACHE_DIR, String Update_Path, Path PACK_NAME) throws IOException {
        RpmtwUpdateMod.LOGGER.info("正在準備檢測資源包版本，最新版本:" + Latest_ver);
        if (!Files.isDirectory(CACHE_DIR)) { //如果沒有資源包路徑
            Files.createDirectories(CACHE_DIR);
        }
        if (!Files.exists(Paths.get(CACHE_DIR + "/Update.txt"))) { //如果沒有更新檔案
            Files.createFile(Paths.get(CACHE_DIR + "/Update.txt")); //建立更新檔案
            File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
        }
        FileReader fr = new FileReader(Update_Path);
        BufferedReader br = new BufferedReader(fr); //讀取舊的版本
        int Old_ver = 0;
        while (br.ready()) {
            Old_ver = Integer.parseInt(br.readLine());
        }
        fr.close();
        try {
            if (Integer.parseInt(Latest_ver_n) > Old_ver + Configer.Update_interval.get() || !Files.exists(PACK_NAME)) {
                SendMsg.send("§6偵測到資源包版本過舊，正在進行更新並重新載入中...。目前版本為:" + Old_ver + "最新版本為:" + Latest_ver_n);
                File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
                FileUtils.copyURLToFile(new URL(json.loadJson("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest").toString()), PACK_NAME.toFile()); //下載資源包檔案
            } else {
                RpmtwUpdateMod.LOGGER.info("目前的RPMTW版本已經是最新的了!!");
            }
            Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder());
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤" + e);
        }
    }
}
