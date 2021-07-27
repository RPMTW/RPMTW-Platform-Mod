package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.net.URL;
import java.nio.file.Path;

public class ReloadPack {
    static Path PackFile = PackVersionCheck.PackFile; //資源包檔案位置

    public ReloadPack() {
        SendMsg.send("正在更新翻譯包中，請稍後...");
        Thread thread = new Thread(() -> {
            try {
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new PackFinder()); //新增資源包至資源包列表
                Minecraft.getInstance().reloadResourcePacks(); //重新載入資源
                SendMsg.send("§b處理完成。");
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
            }
        });
        thread.start();
    }
}
