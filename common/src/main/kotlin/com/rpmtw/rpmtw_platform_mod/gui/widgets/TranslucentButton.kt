package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class TranslucentButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
    onPress: OnPress,
    val tooltip: Component?,
) : Button(x, y, width, height, message, onPress, DEFAULT_NARRATION) {
    override fun renderWidget(poseStack: PoseStack, i: Int, j: Int, f: Float) {
        val minecraft = Minecraft.getInstance()
        setAlpha(0.5f)

        fill(poseStack, x, y, x + width, y + height, -0x80000000 or (Math.round(alpha * 255) shl 16 shl 8))
        renderString(poseStack, minecraft.font, 0XFFFFFF)

        if (this.isHoveredOrFocused && tooltip != null) {
            minecraft.screen?.renderTooltip(
                poseStack, tooltip, i, j
            )
        }
    }
}