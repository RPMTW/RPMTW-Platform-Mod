package siongsng.rpmtwupdatemod.Packs;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackVersionCheck {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.18"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.18.zip"); //資源包檔案位置

    public PackVersionCheck() {
        try {
            RpmtwUpdateMod.LOGGER.info("正在下載 RPMTW 翻譯包");
            if (!Files.isDirectory(PackDir)) { //如果沒有資源包路徑
                Files.createDirectories(PackDir);
            }
            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
            Minecraft.getInstance().getResourcePackRepository().addPackFinder(new PackFinder()); //新增資源包至資源包列表
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
        }
    }
}
