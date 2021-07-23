package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.PackFinder;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.json;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackVersionCheck {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.16.zip"); //資源包檔案位置
    public String UpdateFile = PackDir + "/Update.txt"; //更新暫存檔案放置位置
    public String Latest_ver = json.ver("https://api.github.com/repos/RPMTW/ResourcePack-Mod-zh_tw/releases/latest").toString(); //取得最新版本Tag
    public String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1]; //取得數字ID

    public PackVersionCheck() {
        try {
            RpmtwUpdateMod.LOGGER.info("正在準備檢測資源包版本，最新版本:" + Latest_ver);
            if (!Files.isDirectory(PackDir)) { //如果沒有資源包路徑
                Files.createDirectories(PackDir);
            }
            if (!Files.exists(Paths.get(PackDir + "/Update.txt"))) { //如果沒有更新檔案
                Files.createFile(Paths.get(PackDir + "/Update.txt")); //建立更新檔案
                FileWriter.Writer(Latest_ver_n, UpdateFile); //寫入最新版本
            }
            FileWriter.Writer(Latest_ver_n, UpdateFile); //寫入最新版本
            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
            Minecraft.getInstance().getResourcePackRepository().addPackFinder(new PackFinder()); //新增資源包至資源包列表
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
        }
    }
}
