package siongsng.rpmtwupdatemod.Packs;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.function.SendMsg;

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
//                reloadLanguage();
                SendMsg.send("§b處理完成。");
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
            }
        });
        thread.start();
    }


//    private CompletableFuture<Void> reloadLanguage() {
//        var mc = Minecraft.getInstance();
//        var completableFeature = new CompletableFuture<Void>();
//        var resourceManager = mc.getResourceManager();
//        var resourcePackManager = mc.getResourcePackRepository();
//        var langManager = mc.getLanguageManager();
//        var list = resourcePackManager.openAllSelected();
//
//
//        if (resourceManager instanceof SimpleReloadableResourceManager rm) {
//            mc.reloadStateTracker.reload(ResourceLoadStateTracker.ReloadReason.MANUAL, list);
//            rm.close();
//            for (var pack : list) {
//                try {
//                    rm.add(pack);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    completableFeature.complete(null);
//                    return completableFeature;
//                }
//            }
//            var profiledResourceReload = resourceManager.createReload(
//                    resourceManager,
//                    List.of(langManager),
//                    Util.backgroundExecutor(),
//                    mc,
//                    resourcePackManager.openAllSelected()
//            );
//
//            mc.setOverlay(new LoadingOverlay(
//                    mc,
//                    profiledResourceReload,
//                    throwable -> Util.ifElse(throwable, mc::reloadResourcePacks, () -> {
//                        mc.levelRenderer.allChanged();
//                        mc.reloadStateTracker().finishReload();
//                        completableFeature.complete(null);
//                    }),
//                    true)
//            );
//
//            return completableFeature;
//        } else {
//            throw new IllegalStateException("This method had been called in valid moment, please report this error to RPMTW Update Mod (https://www.rpmtw.ga).");
//        }
//    }
}
