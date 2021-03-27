package siongsng.rpmtwupdatemod.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.File;

@Mod.EventBusSubscriber
public class Config {
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT;

    static {
        crowdinConfig.init(CLIENT_BUILDER);
        reloadpackConfig.init(CLIENT_BUILDER);
        CLIENT = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        RpmtwUpdateMod.LOGGER.info("載入config中... " + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        RpmtwUpdateMod.LOGGER.info("建立config中... " + path);
        file.load();
        RpmtwUpdateMod.LOGGER.info("載入完成config " + path);
        config.setConfig(file);
    }

}