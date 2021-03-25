package siongsng.rpmtwupdatemod.packs;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;

public class LoadPack implements ResourcePackProvider {
    public static final LoadPack RESOUCE = new LoadPack("Resource Pack", new File(System.getProperty("user.home") + "/.rpmtw/1.16/RPMTW-1.16.zip"));

    private final File loaderDirectory;
    public LoadPack(String type, File loaderDirectory) {

        this.loaderDirectory = loaderDirectory;
    }
    public static void init(Set<ResourcePackProvider> set){
        set.add(RESOUCE);
    }
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory){
        final String packName = "RPMTW-1.16";
        final ResourcePackProfile packInfo = ResourcePackProfile.of(packName, true, () -> new ZipResourcePack(loaderDirectory), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}