package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReloadPack {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    final static Path PackFile = PackDir.resolve("RPMTW-1.16.zip");

    public ReloadPack() {
        SendMsg.send("正在執行翻譯包更新中，請稍後...");
        Thread thread = new Thread(() -> {
            try {
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                MinecraftClient.getInstance().reloadResources();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            SendMsg.send("§b處理完成。");
        });
        thread.start();
    }

}