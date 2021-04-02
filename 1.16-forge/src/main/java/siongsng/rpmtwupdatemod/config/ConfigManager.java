package siongsng.rpmtwupdatemod.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {

    /**
     * 此類部分原始碼取至:
     * https://leo3418.github.io/zh/2020/09/09/forge-mod-config-screen.html
     */

    private static final ConfigManager INSTANCE;

    private static final ForgeConfigSpec SPEC;

    private static final Path CONFIG_PATH =
            Paths.get(FMLPaths.CONFIGDIR.get().resolve("rpmtw_update_mod-client.toml").toString());

    static {
        Pair<ConfigManager, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(ConfigManager::new);
        INSTANCE = specPair.getLeft();
        SPEC = specPair.getRight();
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
        SPEC.setConfig(config);
    }

    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void save() {
        SPEC.save();
    }
}
