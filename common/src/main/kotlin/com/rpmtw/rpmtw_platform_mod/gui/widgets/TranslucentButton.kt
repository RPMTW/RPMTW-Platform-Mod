package com.rpmtw.rpmtw_platform_mod.gui.widgets

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.client.gui.navigation.FocusNavigationEvent.ArrowNavigation
import net.minecraft.network.chat.Component
import kotlin.math.roundToInt

class TranslucentButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
    onPress: OnPress,
    val tooltip: Component?,
) : Button(x, y, width, height, message, onPress, DEFAULT_NARRATION) {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val minecraft = Minecraft.getInstance()
        setAlpha(0.5f)
        guiGraphics.fill(x, y, x + width, y + height, -0x80000000 or ((alpha * 255).roundToInt() shl 16 shl 8))
        renderString(guiGraphics, minecraft.font, 0XFFFFFF)

        if (this.isHoveredOrFocused && tooltip != null) {
            guiGraphics.renderTooltip(minecraft.font, tooltip, i, j)
        }
    }

    override fun nextFocusPath(focusNavigationEvent: FocusNavigationEvent): ComponentPath? {
        // To prevent the user from being unable to execute the previous commands, disable arrow navigation for this button.
        if (focusNavigationEvent is ArrowNavigation) {
            return null
        }

        return super.nextFocusPath(focusNavigationEvent)
    }
}