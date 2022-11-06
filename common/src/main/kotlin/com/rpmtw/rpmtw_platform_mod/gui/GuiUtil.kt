package com.rpmtw.rpmtw_platform_mod.gui

import net.minecraft.client.Minecraft

object GuiUtil {
    const val buttonHeight = 20
    const val buttonWidth = 95

    fun closeScreen() {
        Minecraft.getInstance().setScreen(null)
    }
}