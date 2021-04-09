package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.function.File_Writer;
import siongsng.rpmtwupdatemod.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
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
    private final static String Latest_ver = json.ver().toString();
    private final static String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1];
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerLoader(CallbackInfo info) throws IOException {

        this.providers = new HashSet(this.providers);
        if (!Files.isDirectory(CACHE_DIR)) {
            Files.createDirectories(CACHE_DIR);
        }
        if (!Files.exists(Paths.get(CACHE_DIR + "/Update.txt"))) { //如果沒有更新檔案
            Files.createFile(Paths.get(CACHE_DIR + "/Update.txt")); //建立更新檔案
            File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
        }
        RpmtwUpdateMod.LOGGER.info("正在準備檢測資源包版本，最新版本:" + Latest_ver);
        if (Files.exists(PACK_NAME) || !Files.exists(Paths.get(CACHE_DIR + "/RPMTW-1.16.zip"))) { //如果有資源包檔案
            FileReader fr = new FileReader(Update_Path);
            BufferedReader br = new BufferedReader(fr);
            int Old_ver = 0;
            while (br.ready()) {
                Old_ver = Integer.parseInt(br.readLine());
                System.out.println(br.readLine());
            }
            fr.close();
            if (Integer.parseInt(Latest_ver_n) > Old_ver || !Files.exists(Paths.get(CACHE_DIR + "/RPMTW-1.16.zip"))) {
                RpmtwUpdateMod.LOGGER.info("偵測到資源包版本過舊，正在進行更新中...。最新版本為" + Latest_ver_n);
                File_Writer.Writer(Latest_ver_n, Update_Path); //寫入最新版本
                FileUtils.copyURLToFile(new URL(json.loadJson().toString()), PACK_NAME.toFile()); //下載資源包檔案
            } else {
                RpmtwUpdateMod.LOGGER.info("目前的RPMTW版本已經是最新的了!!");
            }
            try {
                Class.forName("siongsng.rpmtwupdatemod.packs.LoadPack").getMethod("init", Set.class).invoke(null, this.providers);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}