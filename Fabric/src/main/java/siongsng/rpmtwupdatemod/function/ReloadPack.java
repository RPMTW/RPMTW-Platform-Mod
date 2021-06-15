package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.resource.ProfiledResourceReload;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ReloadPack {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.17");
    final static Path PackFile = PackDir.resolve("RPMTW-1.17.zip");

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
                        reloadLanguage();
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
        assert new TranslatableText("text.autoconfig.authme.option.authButton.y").asString().equals("Y 座標");
    }

    private CompletableFuture<Void> reloadLanguage() {
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

            mc.setOverlay(new SplashScreen(
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
            throw new IllegalStateException("This method had been called in valid moment, please report this error to RPMTW.");
        }
    }
}
