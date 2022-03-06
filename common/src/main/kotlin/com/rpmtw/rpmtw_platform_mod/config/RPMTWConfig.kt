package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.handlers.CosmicChatHandler
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import me.shedaniel.autoconfig.gui.ConfigScreenProvider
import me.shedaniel.autoconfig.gui.registry.GuiRegistry
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import me.shedaniel.autoconfig.util.Utils.getUnsafely
import me.shedaniel.autoconfig.util.Utils.setUnsafely
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
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionResult
import java.util.*


object RPMTWConfig {
    private var config: ConfigObject? = null
    fun register() {
        RPMTWPlatformMod.LOGGER.info("Registering config")
        // register config
        AutoConfig.register(ConfigObject::class.java) { definition: Config?, configClass: Class<ConfigObject?>? ->
            JanksonConfigSerializer(definition, configClass)
        }
        val guiRegistry: GuiRegistry = AutoConfig.getGuiRegistry(ConfigObject::class.java)

        // key mapping gui
        guiRegistry.registerPredicateProvider({ i13n, field, config, defaults, _ ->
            if (field.isAnnotationPresent(ConfigEntry.Gui.Excluded::class.java)) return@registerPredicateProvider emptyList()
            val entry: KeyCodeEntry = ConfigEntryBuilder.create().startModifierKeyCodeField(
                TranslatableComponent(i13n),
                getUnsafely(field, config, ModifierKeyCode.unknown())
            ).setModifierDefaultValue {
                getUnsafely(
                    field,
                    defaults
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

    private fun listenOnSave(holder: ConfigHolder<ConfigObject>, beforeConfig: ConfigObject) {
        var beforeAccountType = beforeConfig.cosmicChat.accountType

        holder.registerSaveListener { _, edited ->
            var reset = false
            if (edited.cosmicChat.accountType != beforeAccountType) {
                // If change the account type to RPMTW and are not logged RPMTW account, it will not restart the cosmic chat client.
                if (!(!edited.base.isLogin() && edited.cosmicChat.accountType.isRPMTW)) {
                    reset = true
                }
            }

            if (reset) {
                CosmicChatHandler.reset()
            }
            beforeAccountType = edited.cosmicChat.accountType

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
    fun getScreen(parent: Screen?): Screen? {
        if (parent == null) {
            return null
        }

        val provider = AutoConfig.getConfigScreen(ConfigObject::class.java, parent) as ConfigScreenProvider<*>
        provider.setI13nFunction { "config.rpmtw_platform_mod" }
        provider.setBuildFunction { builder: ConfigBuilder ->
            builder.setGlobalized(true)
            builder.setGlobalizedExpanded(false)
            builder.setAfterInitConsumer { screen ->
                val globalizedScreen: GlobalizedClothConfigScreen = screen as GlobalizedClothConfigScreen

                val entry = LoginButtonEntry()
                entry.setScreen(screen)
                @Suppress("UNCHECKED_CAST") globalizedScreen.listWidget.children()
                    .add(0, entry as AbstractConfigEntry<AbstractConfigEntry<*>>)
            }.build()
        }
        return provider.get()
    }

    fun save() {
        AutoConfig.getConfigHolder(get().javaClass).save()
    }
}

internal class LoginButtonEntry : AbstractConfigListEntry<Any?>(TextComponent(UUID.randomUUID().toString()), false) {

    private var widgets: List<AbstractWidget> = listOf()
    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button

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
        loginButton =
            Button(entryWidth / 2 - 30, y, 65, 20, TranslatableComponent("auth.rpmtw_platform_mod.button.login"), {
                RPMTWAuthHandler.login()
            }, { _, matrixStack, i, j ->
                Minecraft.getInstance().screen?.renderTooltip(
                    matrixStack, TranslatableComponent("auth.rpmtw_platform_mod.button.login.tooltip"), i, j
                )
            })

        logoutButton = Button(
            entryWidth / 2 + 40, y, 65, 20, TranslatableComponent("auth.rpmtw_platform_mod.button.logout")
        ) {
            RPMTWAuthHandler.logout()
        }

        widgets = listOf<AbstractWidget>(loginButton, logoutButton)
        loginButton.render(matrices, mouseX, mouseY, delta)
        logoutButton.render(matrices, mouseX, mouseY, delta)

        val text =
            I18n.get(if (RPMTWConfig.get().base.isLogin()) "auth.rpmtw_platform_mod.status.logged_in" else "auth.rpmtw_platform_mod.status.not_logged_in")

        Minecraft.getInstance().font.drawShadow(
            matrices,
            text,
            (x - 4 + entryWidth / 2 - Minecraft.getInstance().font.width(text) / 2).toFloat(),
            (y + 28).toFloat(),
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
        return 35
    }
}
