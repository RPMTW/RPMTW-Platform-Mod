package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Checkbox
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
) :
    Checkbox(x, y, width, height, message, checked, showMessage) {
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

    override fun renderWidget(matrices: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        super.renderWidget(matrices, mouseX, mouseY, delta)
        if (this.isHovered) {
            val client: Minecraft = Minecraft.getInstance()
            client.screen?.renderTooltip(matrices, Component.literal(tooltip), mouseX, mouseY)
        }
    }
}