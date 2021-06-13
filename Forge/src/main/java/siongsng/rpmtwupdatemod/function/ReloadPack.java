package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.PackFinder;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReloadPack {
    static Path PackDir = PackVersionCheck.PackDir; //資源包的放置根目錄
    static Path PackFile = PackVersionCheck.PackFile; //資源包檔案位置
    String UpdateFile = PackDir + "/Update.txt"; //更新暫存檔案放置位置
    String Latest_ver = json.ver("https://api.github.com/repos/RPMTW/ResourcePack-Mod-zh_tw/releases/latest").toString(); //取得最新版本Tag
    String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1]; //取得數字ID

    public ReloadPack() {
        SendMsg.send("由於你按下了檢測翻譯包更新快捷鍵，因此開始進行翻譯包更新檢測作業，請稍後...");
        Thread thread = new Thread(() -> {
            try {
                FileReader fr = new FileReader(UpdateFile);
                BufferedReader br = new BufferedReader(fr); //讀取舊的版本

                int Old_ver = 0;
                while (br.ready()) {
                    Old_ver = Integer.parseInt(br.readLine()); //解析文字
                }
                fr.close();

                if (Integer.parseInt(Latest_ver_n) > Old_ver + Configer.Update_interval.get() || !Files.exists(PackFile)) { //判斷是否要更新(如果最新版本大於現在版本、如果不存在資源包檔案)
                    SendMsg.send("§6偵測到翻譯包版本過舊，正在進行更新並重新載入中...。目前版本為:" + Old_ver + "，最新版本為:" + Latest_ver_n);
                    FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                    FileWriter.Writer(Latest_ver_n, UpdateFile); //寫入最新版本
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
