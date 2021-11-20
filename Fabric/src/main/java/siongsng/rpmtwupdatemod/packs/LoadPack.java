package siongsng.rpmtwupdatemod.packs;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

public class LoadPack implements ResourcePackProvider {

    private static ResourcePackProfile profile;
    private static ZipResourcePack pack;
    private File loaderDirectory;

    public LoadPack() {

        this.loaderDirectory = new File(System.getProperty("user.home") + "/.rpmtw/1.18/RPMTW-1.18.zip");
    }

    public void init(Set<ResourcePackProvider> set) {
        set.add(this);
    }

    public void close() {
        pack.close();
        profile.close();
    }

    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        final String packName = "RPMTW 1.18 翻譯包";
        pack = new ZipResourcePack(loaderDirectory);
        final ResourcePackProfile packInfo = ResourcePackProfile.of(packName, true, () -> pack, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_NONE);
        if (packInfo != null) {
            profile = packInfo;
            consumer.accept(packInfo);
        }
    }
}