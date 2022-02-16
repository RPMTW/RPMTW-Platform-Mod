package com.rpmtw.rpmtw_platform_mod.utilities;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin;
import com.rpmtw.rpmtw_platform_mod.gui.ConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.gui.screens.Screen;

public class RPMTWConfig {
    private static ConfigScreen config;

    public static void register() {
        AutoConfig.register(ConfigScreen.class, JanksonConfigSerializer::new); // register config
        config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
        RPMTWPlatformModPlugin.registerConfig();
    }

    public static ConfigScreen get() {
        if (config == null) {
            register();
        }

        return config;
    }

    public static Screen getScreen(Screen parent) {
        ConfigScreenProvider<ConfigScreen> provider = (ConfigScreenProvider<ConfigScreen>) AutoConfig.getConfigScreen(ConfigScreen.class, parent);
        provider.setI13nFunction(manager -> "config.rpmtw_platform_mod");
        provider.setBuildFunction(builder -> {
            builder.setGlobalized(true);
            builder.setGlobalizedExpanded(false);
            return builder.setAfterInitConsumer(screen -> {}).build();
        });
        return provider.get();
    }

    public static void save() {
        AutoConfig.getConfigHolder(get().getClass()).save();
    }
}
