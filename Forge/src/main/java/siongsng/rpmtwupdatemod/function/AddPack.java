package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.data.MetadataSerializer;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.nio.file.Path;
import java.nio.file.Paths;


public class AddPack {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.12"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.12.zip"); //資源包檔案位置

    public AddPack() {

    }

}
