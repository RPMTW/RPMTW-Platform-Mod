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
    val tooltip: Component?
) : Button(x, y, width, height, message, onPress) {

    override fun blit(stack: PoseStack, x: Int, y: Int, textureX: Int, textureY: Int, width: Int, height: Int) {
        fill(stack, x, y, x + width, y + height, Int.MIN_VALUE)
    }

    override fun renderButton(poseStack: PoseStack, i: Int, j: Int, f: Float) {
        super.renderButton(poseStack, i, j, f)
        if (this.isHovered && tooltip != null) {
            Minecraft.getInstance().screen?.renderTooltip(
                poseStack,
                tooltip,
                i,
                j
            )
        }
    }
}