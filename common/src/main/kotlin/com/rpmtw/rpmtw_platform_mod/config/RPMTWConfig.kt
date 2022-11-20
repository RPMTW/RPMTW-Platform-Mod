package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import com.rpmtw.rpmtw_platform_mod.handlers.UniverseChatHandler
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
import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.GlobalizedClothConfigScreen
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import java.util.*

object RPMTWConfig {
    private var config: ConfigObject? = null


    fun register() {
        RPMTWPlatformMod.LOGGER.info("Registering config")
        // register config
        AutoConfig.register(ConfigObject::class.java) { definition: Config?, configClass: Class<ConfigObject?>? ->
            JanksonConfigSerializer(definition, configClass, buildJankson(Jankson.builder()))
        }
        val guiRegistry: GuiRegistry = AutoConfig.getGuiRegistry(ConfigObject::class.java)

        // key mapping gui
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

        val holder: ConfigHolder<ConfigObject> = AutoConfig.getConfigHolder(ConfigObject::class.java)
        config = holder.config
        listenOnSave(holder, holder.config)

        RPMTWPlatformModPlugin.registerConfigScreen()
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
        var oldRPMTWAuthToken = oldConfig.base.rpmtwAuthToken

        holder.registerSaveListener { _, edited ->
            if (edited.universeChat.accountType != oldAccountType || edited.base.rpmtwAuthToken != oldRPMTWAuthToken) {
                UniverseChatHandler.restart()
            }

            // Save the new configs
            oldAccountType = edited.universeChat.accountType
            oldRPMTWAuthToken = edited.base.rpmtwAuthToken

            return@registerSaveListener InteractionResult.SUCCESS
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

            builder.setAfterInitConsumer { screen ->
                val globalizedScreen: GlobalizedClothConfigScreen = screen as GlobalizedClothConfigScreen

                val entry = RPMTWAccountEntry()
                entry.setScreen(screen)
                @Suppress("UNCHECKED_CAST") globalizedScreen.listWidget.children()
                    .add(0, entry as AbstractConfigEntry<AbstractConfigEntry<*>>)
            }

            builder.build()
        }
        return provider.get()
    }

    fun save() {
        AutoConfig.getConfigHolder(get().javaClass).save()
    }
}

internal class RPMTWAccountEntry : AbstractConfigListEntry<Any?>(Component.literal("rpmtw_account"), false) {

    private var widgets: List<AbstractWidget> = listOf()

    override fun save() {}

    override fun isMouseInside(mouseX: Int, mouseY: Int, x: Int, y: Int, entryWidth: Int, entryHeight: Int): Boolean {
        return false
    }

    override fun render(
        matrices: PoseStack,
        index: Int,
        y: Int,
        x: Int,
        entryWidth: Int,
        entryHeight: Int,
        mouseX: Int,
        mouseY: Int,
        isHovered: Boolean,
        delta: Float
    ) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta)

        val title = Component.translatable("auth.rpmtw_platform_mod.title")
        val isLogin = RPMTWConfig.get().base.isLogin()
        val authStatus: String = if (isLogin) {
            I18n.get("auth.rpmtw_platform_mod.status.logged_in")
        } else {
            I18n.get("auth.rpmtw_platform_mod.status.not_logged_in")
        }

        if (!isLogin) {
            val loginButton = Button(entryWidth / 2 + 20,
                y + 10,
                65,
                20,
                Component.translatable("auth.rpmtw_platform_mod.button.login"),
                {
                    RPMTWAuthHandler.login()
                },
                { _, matrixStack, i, j ->
                    Minecraft.getInstance().screen?.renderTooltip(
                        matrixStack, Component.translatable("auth.rpmtw_platform_mod.button.login.tooltip"), i, j
                    )
                })

            widgets = listOf(loginButton)
            loginButton.render(matrices, mouseX, mouseY, delta)
        } else {
            val logoutButton = Button(
                entryWidth / 2 + 20, y + 10, 65, 20, Component.translatable("auth.rpmtw_platform_mod.button.logout")
            ) {
                RPMTWAuthHandler.logout()
            }

            widgets = listOf(logoutButton)
            logoutButton.render(matrices, mouseX, mouseY, delta)
        }

        val font = Minecraft.getInstance().font

        font.drawShadow(
            matrices,
            title,
            (x - 4 + entryWidth / 2 - Minecraft.getInstance().font.width(title) / 2).toFloat(),
            (y).toFloat(),
            -1
        )

        font.drawShadow(
            matrices,
            authStatus,
            (x - 4 + entryWidth / 2 - Minecraft.getInstance().font.width(authStatus) / 2).toFloat(),
            (y + 33).toFloat(),
            -1
        )
    }

    override fun children(): List<GuiEventListener?> {
        return widgets
    }

    override fun narratables(): List<NarratableEntry?> {
        return widgets
    }

    override fun getValue(): Any? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    override fun getDefaultValue(): Optional<Any?> {
        return Optional.of(Any()) as Optional<Any?>
    }

    override fun getItemHeight(): Int {
        return 45
    }
}
