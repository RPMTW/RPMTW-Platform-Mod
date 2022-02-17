package com.rpmtw.rpmtw_platform_mod.forge

import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig.getScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory
import net.minecraftforge.fml.ModLoadingContext

@Suppress("unused")
object RPMTWPlatformModPluginImpl {
    @JvmStatic
    fun registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory::class.java) {
            ConfigGuiFactory { _: Minecraft?, screen: Screen? -> getScreen(screen) }
        }
    }
}