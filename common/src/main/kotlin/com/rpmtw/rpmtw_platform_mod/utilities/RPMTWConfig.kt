package com.rpmtw.rpmtw_platform_mod.utilities

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.gui.ConfigScreen
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.gui.ConfigScreenProvider
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen

object RPMTWConfig {
    private var config: ConfigScreen? = null
    fun register() {
        RPMTWPlatformMod.LOGGER.info("Registering config")
        AutoConfig.register(ConfigScreen::class.java) { definition: Config?, configClass: Class<ConfigScreen?>? ->
            JanksonConfigSerializer(definition, configClass)
        } // register config
        config = AutoConfig.getConfigHolder(ConfigScreen::class.java).config
        RPMTWPlatformModPlugin.registerConfigScreen()
        RPMTWPlatformMod.LOGGER.info("Registered config")
    }

    @JvmStatic
    fun get(): ConfigScreen {
        if (config == null) {
            register()
        }
        return config!!
    }

    @Suppress("DEPRECATION", "UnstableApiUsage")
    @JvmStatic
    fun getScreen(parent: Screen?): Screen? {
        if (parent == null) {
            return null
        }

        val provider = AutoConfig.getConfigScreen(ConfigScreen::class.java, parent) as ConfigScreenProvider<*>
        provider.setI13nFunction { "config.rpmtw_platform_mod" }
        provider.setBuildFunction { builder: ConfigBuilder ->
            builder.setGlobalized(true)
            builder.setGlobalizedExpanded(false)
            builder.setAfterInitConsumer { }.build()
        }
        return provider.get()
    }

    fun save() {
        AutoConfig.getConfigHolder(get().javaClass).save()
    }
}