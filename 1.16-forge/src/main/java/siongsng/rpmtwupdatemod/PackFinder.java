package siongsng.rpmtwupdatemod;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;

import java.util.function.Consumer;

public class PackFinder implements IPackFinder {
    @Override
    public void findPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory iFactory) { //註冊資源包工廠
        ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack("RPMTW-1.16.zip", //設定資源包資訊
                true, () -> new FilePack(PackVersionCheck.PackFile.toFile()),
                iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
        if (packInfo != null) { //如果資源包資訊不是無效的
            consumer.accept(packInfo); //加入資源包
        }
    }
}
