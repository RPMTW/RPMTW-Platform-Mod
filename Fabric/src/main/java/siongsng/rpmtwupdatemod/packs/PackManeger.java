package siongsng.rpmtwupdatemod.packs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackProvider;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class PackManeger {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    final static Path PackFile = PackDir.resolve("RPMTW-1.16.zip");

    public static LoadPack RESOUCE;
    private static Set<ResourcePackProvider> provider;

    public static void reload() {
        RESOUCE = new LoadPack();
        RESOUCE.init(provider);
    }

    public static void create(Set<ResourcePackProvider> providerSet) {
        RESOUCE = new LoadPack();
        RESOUCE.init(providerSet);
        provider = providerSet;
    }

    public static void close() {
        if (RESOUCE != null)
            RESOUCE.close();
    }

    public static void ReloadPack() {
        SendMsg.send("正在執行翻譯包更新中，請稍後...");
        File packFile = PackFile.toFile();
        Thread thread = new Thread(() -> {

            try {
                close();
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), packFile); //下載資源包檔案
                ZipUtils.removeDirFromZip(packFile, Configer.getConfig().modBlackList);
                reload();
                MinecraftClient.getInstance().reloadResources();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
        thread.start();
    }

    public static void PackVersionCheck(Set<ResourcePackProvider> providers) throws IOException {
        if (!Files.isDirectory(PackDir)) {
            Files.createDirectories(PackDir);
        }
        File packFile = PackFile.toFile();
        try {
            if (RESOUCE == null) {
                RpmtwUpdateMod.LOGGER.info("正在準備檢測資源包版本。");
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), packFile); //下載資源包檔案
                ZipUtils.removeDirFromZip(packFile, Configer.getConfig().modBlackList);
                create(providers);
            }
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e);
        }
    }

}