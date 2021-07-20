package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class ReloadPack {
    static Path PackFile = AddPack.PackFile; //資源包檔案位置

    public ReloadPack() {
        try {
            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AddPack().setResourcesRepository();
        Minecraft.getMinecraft().getLanguageManager().onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
    }
}
