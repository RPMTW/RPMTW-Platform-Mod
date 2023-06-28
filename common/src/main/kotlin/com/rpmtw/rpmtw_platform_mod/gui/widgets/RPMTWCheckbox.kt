package com.rpmtw.rpmtw_platform_mod.gui.widgets

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.network.chat.Component

class RPMTWCheckbox(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
    checked: Boolean,
    showMessage: Boolean,
    private val onPress: (checked: Boolean) -> Unit,
    private val tooltip: String
) : Checkbox(x, y, width, height, message, checked, showMessage) {
    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        message: Component,
        checked: Boolean,
        onPress: (checked: Boolean) -> Unit,
        tooltip: String
    ) : this(x, y, width, height, message, checked, true, onPress, tooltip)

    override fun onPress() {
        super.onPress()
        onPress(this.selected())
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.renderWidget(guiGraphics, mouseX, mouseY, delta)
        if (this.isHovered) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(tooltip), mouseX, mouseY)
        }
    }

    override fun nextFocusPath(focusNavigationEvent: FocusNavigationEvent): ComponentPath? {
        // To prevent the user from being unable to execute the previous commands, disable arrow navigation for this button.
        if (focusNavigationEvent is FocusNavigationEvent.ArrowNavigation) {
            return null
        }

        return super.nextFocusPath(focusNavigationEvent)
    }
}