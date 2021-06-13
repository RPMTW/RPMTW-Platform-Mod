package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ReloadPack {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    final static Path PackFile = PackDir.resolve("RPMTW-1.16.zip");

    public ReloadPack() {
        SendMsg.send("由於你按下了檢測翻譯包更新快捷鍵，因此開始進行翻譯包更新檢測作業，請稍後...");
        Thread thread = new Thread(() -> {
            if (Files.exists(PackFile)) {
                try {
                    long fileTime = Files.getLastModifiedTime(PackFile).toMillis();
                    long nowTime = System.currentTimeMillis();
                    if (TimeUnit.MILLISECONDS.toMinutes(nowTime - fileTime) < 1) {
                        SendMsg.send("§6偵測到翻譯包版本過舊，正在進行更新並重新載入中...。");
                        FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                        MinecraftClient.getInstance().reloadResources();
                    } else {
                        SendMsg.send("§a目前的RPMTW翻譯包版本已經是最新的了!因此不進行更新作業。");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                SendMsg.send("§b處理完成。");
            }
        });
        thread.start();
    }
}
