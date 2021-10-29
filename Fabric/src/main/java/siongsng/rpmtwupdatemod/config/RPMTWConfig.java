package siongsng.rpmtwupdatemod.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class RPMTWConfig {
    private static ConfigScreen config;

    public static ConfigScreen getConfig() {
        if (config == null) {
            AutoConfig.register(ConfigScreen.class, Toml4jConfigSerializer::new); //註冊Config
            config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
        }

        return config;
    }

    public static void saveConfig() {
        AutoConfig.getConfigHolder(getConfig().getClass()).save();
    }
}
