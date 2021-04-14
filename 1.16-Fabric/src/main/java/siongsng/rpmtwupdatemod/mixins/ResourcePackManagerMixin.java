package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;
import siongsng.rpmtwupdatemod.json;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {

    private final static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    private static final Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    private final static String Update_Path = CACHE_DIR + "/Update.txt";
    private final static String Latest_ver = json.ver("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest").toString();
    private final static String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1];
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerLoader(CallbackInfo info) throws IOException {
        this.providers = new HashSet(this.providers);
        new PackVersionCheck(this.providers, Latest_ver, Latest_ver_n, CACHE_DIR, Update_Path, PACK_NAME);
    }
}