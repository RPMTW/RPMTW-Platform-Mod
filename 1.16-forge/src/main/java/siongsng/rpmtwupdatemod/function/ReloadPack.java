package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class ReloadPack {
    public ReloadPack() {
        try {
            FileReader fr = new FileReader(RpmtwUpdateMod.Update_Path);
            BufferedReader br = new BufferedReader(fr);
            int Old_ver = 0;
            while (br.ready()) {
                Old_ver = Integer.parseInt(br.readLine());
            }
            fr.close();
            if (Integer.parseInt(RpmtwUpdateMod.Latest_ver_n) > Old_ver + Configer.Update_interval.get()) {
                SendMsg.send("§6偵測到資源包版本過舊，正在進行更新並重新載入中...。最新版本為" + RpmtwUpdateMod.Latest_ver_n);
                File_Writer.Writer(RpmtwUpdateMod.Latest_ver_n, RpmtwUpdateMod.Update_Path); //寫入最新版本
                FileUtils.copyURLToFile(new URL(json.loadJson().toString()), RpmtwUpdateMod.PACK_NAME.toFile()); //下載資源包檔案
                Minecraft.getInstance().reloadResources(); //重新載入資源
            } else {
                SendMsg.send("§a目前的RPMTW版本已經是最新的了!!因此不重新載入翻譯。");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SendMsg.send("§a處理完成。");
    }
}
