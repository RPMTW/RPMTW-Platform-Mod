package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.resources.ResourcePackRepository;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackVersionCheck {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.12"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.12.zip"); //資源包檔案位置

    public PackVersionCheck() {
        try {
            if (!Files.isDirectory(PackDir)) { //如果沒有資源包路徑
                Files.createDirectories(PackDir);
            }
            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
            new AddPack();
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
        }
    }
}

