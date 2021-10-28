package siongsng.rpmtwupdatemod.packs;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;

import java.io.File;
import java.util.function.Consumer;

public class PackFinder implements IPackFinder {
    private File loaderDirectory;
    private static ResourcePackInfo packInfo;
    private static FilePack pack;

    public PackFinder() {
        this.loaderDirectory = PacksManager.PackFile.toFile();
    }

    public void close() {
        if (packInfo != null && pack != null) {
            pack.close();
            packInfo.close();
        }
    }

    @Override
    public void findPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory iFactory) { //註冊資源包工廠
        pack = new FilePack(loaderDirectory);
        ResourcePackInfo resPackInfo = ResourcePackInfo.createResourcePack("RPMTW 1.16 翻譯包", //設定資源包資訊
                true, () -> pack,
                iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
        if (resPackInfo != null) { //如果資源包資訊不是無效的
            packInfo = resPackInfo;
            consumer.accept(resPackInfo);  //加入資源包
        }
    }
}
