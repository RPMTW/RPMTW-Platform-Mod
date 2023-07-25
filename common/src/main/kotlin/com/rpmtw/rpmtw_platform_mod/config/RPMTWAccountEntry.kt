package com.rpmtw.rpmtw_platform_mod.config

import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig.get
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler.login
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler.logout
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import java.util.*


class RPMTWAccountEntry : AbstractConfigListEntry<Any?>(
    Component.literal(
        UUID.randomUUID().toString()
    ), false
) {
    private val widgets: MutableList<AbstractWidget?> = ArrayList()
    override fun getItemHeight(): Int {
        return 48
    }

    override fun nextFocusPath(focusNavigationEvent: FocusNavigationEvent): ComponentPath? {
        return null
    }

    override fun narratables(): List<NarratableEntry?> {
        return widgets
    }

    override fun getSearchTags(): Iterator<String> {
        return Collections.emptyIterator()
    }

    override fun getValue(): Any {
        return Any()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getDefaultValue(): Optional<Any?> {
        return Optional.of(Any()) as Optional<Any?>
    }

    override fun isMouseInside(mouseX: Int, mouseY: Int, x: Int, y: Int, entryWidth: Int, entryHeight: Int): Boolean {
        return false
    }

    override fun render(
        guiGraphics: GuiGraphics,
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
        super.render(guiGraphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta)
        val isLogin = get().isLogin()
        val authStatus: String = if (isLogin) {
            I18n.get("auth.rpmtw_platform_mod.status.logged_in")
        } else {
            I18n.get("auth.rpmtw_platform_mod.status.not_logged_in")
        }

        if (!isLogin) {
            val loginButton = Button.Builder(
                Component.translatable("auth.rpmtw_platform_mod.button.login")
            ) { _ ->
                login(
                    RPMTWAuthHandler.port
                )
            }.bounds(entryWidth / 2 + 20, y + 15, 65, 20)
                .tooltip(Tooltip.create(Component.translatable("auth.rpmtw_platform_mod.button.login.tooltip"))).build()
            loginButton.render(guiGraphics, mouseX, mouseY, delta)
            widgets.add(loginButton)
        } else {
            val logoutButton = Button.builder(
                Component.translatable("auth.rpmtw_platform_mod.button.logout")
            ) { _ -> logout() }.bounds(entryWidth / 2 + 20, y + 15, 65, 20).build()
            logoutButton.render(guiGraphics, mouseX, mouseY, delta)
            widgets.add(logoutButton)
        }

        val font = Minecraft.getInstance().font
        guiGraphics.drawString(
            font, authStatus, x - 4 + entryWidth / 2 - Minecraft.getInstance().font.width(authStatus) / 2, y, -1
        )
    }

    override fun children(): List<GuiEventListener?> {
        return widgets
    }
}