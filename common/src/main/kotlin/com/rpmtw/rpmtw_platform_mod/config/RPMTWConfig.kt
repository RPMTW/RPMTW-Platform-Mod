package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.platform.InputConstants
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
import dev.architectury.platform.Platform
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import me.shedaniel.autoconfig.gui.ConfigScreenProvider
import me.shedaniel.autoconfig.gui.registry.GuiRegistry
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import me.shedaniel.autoconfig.util.Utils.getUnsafely
import me.shedaniel.autoconfig.util.Utils.setUnsafely
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonObject
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult


object RPMTWConfig {
    private var config: ConfigObject? = null
    private const val PROTOCOL_VERSION = 1


    private fun register() {
        RPMTWPlatformMod.LOGGER.info("Registering config")
        // Register config
        AutoConfig.register(ConfigObject::class.java) { definition: Config?, configClass: Class<ConfigObject?>? ->
            JanksonConfigSerializer(definition, configClass, buildJankson(Jankson.builder()))
        }
        val guiRegistry: GuiRegistry = AutoConfig.getGuiRegistry(ConfigObject::class.java)

        // Key mapping gui
        guiRegistry.registerPredicateProvider({ i13n, field, config, defaults, _ ->
            if (field.isAnnotationPresent(ConfigEntry.Gui.Excluded::class.java)) return@registerPredicateProvider emptyList()
            val entry: KeyCodeEntry = ConfigEntryBuilder.create().startModifierKeyCodeField(
                Component.translatable(i13n), getUnsafely(field, config, ModifierKeyCode.unknown())
            ).setModifierDefaultValue {
                getUnsafely(
                    field, defaults
                )
            }.setModifierSaveConsumer { newValue -> setUnsafely(field, config, newValue) }.build()
            listOf(entry)
        }) { field -> field.type === ModifierKeyCode::class.java }

        val holder = AutoConfig.getConfigHolder(ConfigObject::class.java)
        config = holder.config

        checkProtocolVersion(holder, holder.config)
        listenOnSave(holder, holder.config)

        RPMTWPlatformMod.LOGGER.info("Registered config")
    }

    private fun buildJankson(builder: Jankson.Builder): Jankson {
        // https://github.com/shedaniel/RoughlyEnoughItems/blob/b0af2a1f18414a182ab3a8c0c8d7d97b5f56e8c9/runtime/src/main/java/me/shedaniel/rei/impl/client/config/ConfigManagerImpl.java#L166
        // ModifierKeyCode
        builder.registerSerializer(ModifierKeyCode::class.java) { value, _ ->
            val `object` = JsonObject()
            `object`["keyCode"] = JsonPrimitive(value.keyCode.name)
            `object`["modifier"] = JsonPrimitive(value.modifier.value)
            `object`
        }

        builder.registerDeserializer(
            JsonObject::class.java, ModifierKeyCode::class.java
        ) { value, _ ->
            val code = value[String::class.java, "keyCode"]
            if (code!!.endsWith(".unknown")) {
                return@registerDeserializer ModifierKeyCode.unknown()
            } else {
                val keyCode: InputConstants.Key = InputConstants.getKey(code)
                val modifier = Modifier.of(value.getShort("modifier", 0.toShort()))
                return@registerDeserializer ModifierKeyCode.of(keyCode, modifier)
            }
        }

        return builder.build()
    }

    private fun listenOnSave(holder: ConfigHolder<ConfigObject>, oldConfig: ConfigObject) {
        var oldAccountType = oldConfig.universeChat.accountType
        var oldRPMTWAuthToken = oldConfig.rpmtwAuthToken

        holder.registerSaveListener { _, edited ->
            if (edited.universeChat.accountType != oldAccountType || edited.rpmtwAuthToken != oldRPMTWAuthToken) {
                UniverseChatHandler.restart()
            }

            // Save the new configs
            oldAccountType = edited.universeChat.accountType
            oldRPMTWAuthToken = edited.rpmtwAuthToken

            return@registerSaveListener InteractionResult.SUCCESS
        }
    }

    private fun checkProtocolVersion(holder: ConfigHolder<ConfigObject>, config: ConfigObject) {
        if (config.protocolVersion == null) {
            holder.config.protocolVersion = PROTOCOL_VERSION

            if (!config.firstJoinLevel) {
                holder.resetToDefault()
            }

            holder.save()
        }
    }

    @JvmStatic
    fun get(): ConfigObject {
        if (config == null) {
            register()
        }
        return config!!
    }

    @Suppress("DEPRECATION", "UnstableApiUsage")
    @JvmStatic
    fun getScreen(parent: Screen? = null): Screen? {
        val provider = AutoConfig.getConfigScreen(ConfigObject::class.java, parent) as ConfigScreenProvider<*>
        provider.setI13nFunction { "config.rpmtw_platform_mod" }
        provider.setBuildFunction { builder: ConfigBuilder ->
            builder.setGlobalized(true)
            builder.setGlobalizedExpanded(false)

            if (!Platform.isForge()) {
                val category =
                    builder.getOrCreateCategory(Component.translatable("auth.rpmtw_platform_mod.title"))
                category.addEntry(RPMTWAccountEntry())
            }

            builder.build()
        }
        return provider.get()
    }

    fun save() {
        AutoConfig.getConfigHolder(get().javaClass).save()
    }
}