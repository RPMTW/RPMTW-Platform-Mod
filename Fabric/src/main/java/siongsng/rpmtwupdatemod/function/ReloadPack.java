package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.resource.ProfiledResourceReload;
import net.minecraft.resource.ReloadableResourceManagerImpl;
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

public class ReloadPack {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.17");
    final static Path PackFile = PackDir.resolve("RPMTW-1.17.zip");

    public ReloadPack() {
        SendMsg.send("由於你按下了翻譯包更新快捷鍵，正在執行更新中，請稍後...");
        Thread thread = new Thread(() -> {
            if (Files.exists(PackFile)) {
                try {
                        FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
                        reloadLanguage();
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
