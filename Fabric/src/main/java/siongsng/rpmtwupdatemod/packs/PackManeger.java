package siongsng.rpmtwupdatemod.packs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.resource.ProfiledResourceReload;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.Register.RPMKeyBinding;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PackManeger {
    public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.17"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.17.zip"); //資源包檔案位置

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
                ZipUtils.removeDirFromZip(packFile, RPMTWConfig.getConfig().modBlackList);
                reload();
                reloadLanguage().whenComplete((v, t) -> {
                    if (RPMKeyBinding.updateLock) {
                        RPMKeyBinding.updateLock = false;
                        SendMsg.send("§b處理完成。");
                    }
                });
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        });
        thread.start();
    }

    private static CompletableFuture<Void> reloadLanguage() {
        var mc = MinecraftClient.getInstance();
        var completableFeature = new CompletableFuture<Void>();
        var resourceManager = mc.getResourceManager();
        var resourcePackManager = mc.getResourcePackManager();
        var langManager = mc.getLanguageManager();
        var list = resourcePackManager.createResourcePacks();
        if (resourceManager instanceof ReloadableResourceManagerImpl rm) {
            mc.resourceReloadLogger.reload(ResourceReloadLogger.ReloadReason.MANUAL, list);

            rm.clear();

            for (var pack : list) {
                try {
                    rm.addPack(pack);
                } catch (Exception e) {
                    e.printStackTrace();
                    completableFeature.complete(null);
                    return completableFeature;
                }
            }

            var profiledResourceReload = ProfiledResourceReload.create(
                    resourceManager,
                    List.of(langManager),
                    Util.getMainWorkerExecutor(),
                    mc,
                    MinecraftClient.COMPLETED_UNIT_FUTURE
            );

            mc.setOverlay(new SplashOverlay(
                    mc,
                    profiledResourceReload,
                    throwable -> Util.ifPresentOrElse(throwable, mc::handleResourceReloadException, () -> {
                        mc.worldRenderer.reload();
                        mc.resourceReloadLogger.finish();
                        completableFeature.complete(null);
                    }),
                    true)
            );

            return completableFeature;
        } else {
            throw new IllegalStateException("This method had been called in valid moment, please report this error to RPMTW Update Mod (https://www.rpmtw.ga).");
        }
    }

    public static void PackVersionCheck(Set<ResourcePackProvider> providers) throws IOException {
        if (!Files.isDirectory(PackDir)) {
            Files.createDirectories(PackDir);
        }
        File packFile = PackFile.toFile();
        try {
            if (RESOUCE == null) {
                RpmtwUpdateMod.LOGGER.info("正在準備下載翻譯包...");
                FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), packFile); //下載資源包檔案
                ZipUtils.removeDirFromZip(packFile, RPMTWConfig.getConfig().modBlackList);
                create(providers);
            }
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e);
        }
    }

}