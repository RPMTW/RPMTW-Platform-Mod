package siongsng.rpmtwupdatemod.packs;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

public class LoadPack implements ResourcePackProvider {
    public static final LoadPack RESOUCE = new LoadPack("Resource Pack", PackVersionCheck.PackFile.toFile());

    private final File loaderDirectory;

    public LoadPack(String type, File loaderDirectory) {
        this.loaderDirectory = loaderDirectory;
    }

    public static void init(Set<ResourcePackProvider> set) {
        set.add(RESOUCE);
    }

    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        final String packName = "RPMTW 1.17 翻譯包";
        final ResourcePackProfile packInfo = ResourcePackProfile.of(packName, true, () -> new ZipResourcePack(loaderDirectory), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_NONE);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}