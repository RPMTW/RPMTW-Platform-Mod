package siongsng.rpmtwupdatemod;

import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;

import java.util.function.Consumer;

public class PackFinder implements RepositorySource {
    @Override
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor iFactory) { //註冊資源包工廠
        Pack packInfo = Pack.create("RPMTW 1.17 翻譯包", //設定資源包資訊
                true, () -> new FilePackResources(PackVersionCheck.PackFile.toFile()),
                iFactory, Pack.Position.TOP, PackSource.BUILT_IN);
        if (packInfo != null) { //如果資源包資訊不是無效的
            consumer.accept(packInfo); //加入資源包
        }
    }
}
