package siongsng.rpmtwupdatemod.packs;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

public class LoadPack implements ResourcePackProvider {

    private File loaderDirectory;
    private static ResourcePackProfile profile;
    private static ZipResourcePack pack;
    public LoadPack() {

        this.loaderDirectory = new File(System.getProperty("user.home") + "/.rpmtw/1.16/RPMTW-1.16.zip");
    }

    public void init(Set<ResourcePackProvider> set) {
        set.add(this);
    }
    
    public void close() {
     	pack.close();
    	profile.close();
    }

    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        final String packName = "RPMTW 1.16 翻譯包";
        pack = new ZipResourcePack(loaderDirectory);
        final ResourcePackProfile packInfo = ResourcePackProfile.of(packName, true, () -> pack , factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
        if (packInfo != null) {
        	profile = packInfo;
            consumer.accept(packInfo);
        }
    }
}