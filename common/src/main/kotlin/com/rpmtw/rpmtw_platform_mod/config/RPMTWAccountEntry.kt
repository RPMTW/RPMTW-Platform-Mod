package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
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

    override fun narratables(): List<NarratableEntry?> {
        return widgets
    }

    override fun getValue(): Any {
        return Any()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getDefaultValue(): Optional<Any?> {
        return Optional.of(Any()) as Optional<Any?>
    }

    override fun save() {

    }

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
        val isLogin = RPMTWConfig.get().isLogin()
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

            widgets.add(loginButton)
            loginButton.render(matrices, mouseX, mouseY, delta)
        } else {
            val logoutButton = Button(
                entryWidth / 2 + 20, y + 10, 65, 20, Component.translatable("auth.rpmtw_platform_mod.button.logout")
            ) {
                RPMTWAuthHandler.logout()
            }

            widgets.add(logoutButton)
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
}