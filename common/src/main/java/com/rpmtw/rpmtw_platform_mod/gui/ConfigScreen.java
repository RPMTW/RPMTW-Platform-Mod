package com.rpmtw.rpmtw_platform_mod.gui;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Config(name = "rpmtw_platform_mod")
@Environment(EnvType.CLIENT)
public class ConfigScreen implements ConfigData {

    @ConfigEntry.Category("translate")
    @ConfigEntry.Gui.TransitiveObject
    public Translate translate = new Translate();

    public static class Translate {
        @Comment("啟動遊戲時自動切換遊戲語言至 RPMTranslator 支援的語言")
        public boolean autoToggleLanguage = true;
    }
}

