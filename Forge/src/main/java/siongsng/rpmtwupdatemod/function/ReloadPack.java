package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.PackFinder;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ReloadPack {
    static Path PackFile = PackVersionCheck.PackFile; //資源包檔案位置

    public ReloadPack() {
        SendMsg.send("由於你按下了檢測翻譯包更新快捷鍵，因此開始進行翻譯包更新檢測作業，請稍後...");
        Thread thread = new Thread(() -> {
            try {
                if (Files.exists(PackFile)) {
                    long fileTime = Files.getLastModifiedTime(PackFile).toMillis();
                    long nowTime = System.currentTimeMillis();
                    if (TimeUnit.MILLISECONDS.toMinutes(nowTime - fileTime) < 1) {
                        SendMsg.send("§6偵測到翻譯包版本過舊，正在進行更新並重新載入中...。");
                    }
                    FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                    Minecraft.getInstance().getResourcePackList().addPackFinder(new PackFinder()); //新增資源包至資源包列表
                    Minecraft.getInstance().reloadResources(); //重新載入資源
                } else {
                    SendMsg.send("§a目前的RPMTW翻譯包版本已經是最新的了!因此不進行更新作業。");
                }
                SendMsg.send("§b處理完成。");
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
            }
        });
        thread.start();
    }
}
