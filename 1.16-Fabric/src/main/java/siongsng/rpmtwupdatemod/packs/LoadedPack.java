package siongsng.rpmtwupdatemod.packs;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

public class LoadedPack implements ResourcePackProvider {
    public static final LoadedPack RESOUCE = new LoadedPack("Resource Pack", new File(System.getProperty("user.home") + "/.rpmtw/1.16/rpmtw-1.16.zip"));

    private final File loaderDirectory;
    private LoadedPack(String type, File loaderDirectory) {

        this.loaderDirectory = loaderDirectory;
    }
    public static void init(Set<ResourcePackProvider> set){
        set.add(RESOUCE);
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        final String packName = "RPMTW-1.16";
        final ResourcePackProfile packInfo = ResourcePackProfile.of(packName, true, () -> new ZipResourcePack(loaderDirectory), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}