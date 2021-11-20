package siongsng.rpmtwupdatemod.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod.EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec CLIENT;
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    private static final Path CONFIG_PATH = Paths.get(FMLPaths.CONFIGDIR.get().resolve("rpmtw_update_mod-client.toml").toString());

    static {
        RPMTWConfig.init(CLIENT_BUILDER);
        CLIENT = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config) {
        RpmtwUpdateMod.LOGGER.info("載入config中... " + CONFIG_PATH);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(String.valueOf(CONFIG_PATH))).sync().autosave().writingMode(WritingMode.REPLACE).build();
        RpmtwUpdateMod.LOGGER.info("建立config中... " + CONFIG_PATH);
        file.load();
        RpmtwUpdateMod.LOGGER.info("載入完成config " + CONFIG_PATH);
        config.setConfig(file);
    }

    public static void save() {
        CLIENT.save();
    }
}