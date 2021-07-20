package siongsng.rpmtwupdatemod.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class ConfigScreen extends GuiConfig {
    public ConfigScreen(GuiScreen parentScreen) {
        super(parentScreen, RpmtwUpdateMod.MOD_ID,"RPMTW 設定選單");
    }
}