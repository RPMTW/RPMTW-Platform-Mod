package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent

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

    override fun renderButton(matrices: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        super.renderButton(matrices, mouseX, mouseY, delta)
        if (this.isHovered) {
            val mc: Minecraft = Minecraft.getInstance()
            mc.screen?.renderTooltip(matrices, TextComponent(tooltip), mouseX, mouseY)
        }
    }
}