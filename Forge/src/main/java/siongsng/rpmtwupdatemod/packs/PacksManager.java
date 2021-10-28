package siongsng.rpmtwupdatemod.packs;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class PacksManager {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.16.zip"); //資源包檔案位置
    public static String UpdateFile = PackDir + "/Update.txt"; //更新暫存檔案放置位置
    private static PackFinder RESOURCES;

    public static void close() {
        if (RESOURCES != null)
            RESOURCES.close();
    }

    public static void ReloadPack() {
        SendMsg.send("由於你按下了檢測翻譯包更新快捷鍵，因此開始進行翻譯包更新檢測作業，請稍後...");
        Thread thread = new Thread(() -> {
            try {
                if (Files.exists(PackFile)) {
                    File file = PackFile.toFile();
                    close();
                    RESOURCES = new PackFinder();
                    long fileTime = Files.getLastModifiedTime(PackFile).toMillis();
                    long nowTime = System.currentTimeMillis();
                    if (TimeUnit.MILLISECONDS.toMinutes(nowTime - fileTime) < 1) {
                        SendMsg.send("§6偵測到翻譯包版本過舊，正在進行更新並重新載入中...。");
                    }
                    FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), file); //下載資源包檔案
                    ZipUtils.removeDirFromZip(file, Configer.modBlackList.get());
                    Minecraft.getInstance().getResourcePackList().addPackFinder(RESOURCES); //新增資源包至資源包列表
                    Minecraft.getInstance().reloadResources().whenCompleteAsync((c, t) -> {//重新載入資源
                        RPMKeyBinding.updateLock = false;
                        SendMsg.send("§b處理完成。");
                    });

                } else {
                    SendMsg.send("§a目前的RPMTW翻譯包版本已經是最新的了!因此不進行更新作業。");
                }
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
            }
        });
        thread.start();
    }

    public static void PackVersionCheck() {
        try {
            if (RESOURCES == null) {
                File file = PackFile.toFile();
                RESOURCES = new PackFinder();
                RpmtwUpdateMod.LOGGER.info("正在準備下載翻譯包...");
                if (!Files.isDirectory(PackDir)) { //如果沒有資源包路徑
                    Files.createDirectories(PackDir);
                }
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), file); //下載資源包檔案
                ZipUtils.removeDirFromZip(file, Configer.modBlackList.get());
                Minecraft.getInstance().getResourcePackList().addPackFinder(RESOURCES); //新增資源包至資源包列表
            }
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
        }
    }
}
