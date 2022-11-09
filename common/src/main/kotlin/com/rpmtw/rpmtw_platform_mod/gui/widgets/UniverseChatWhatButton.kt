package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_platform_mod.gui.GuiUtil
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent

class UniverseChatWhatButton(width: Int, height: Int) : Button(
    width / 2 + 50, height / 2 + 30, GuiUtil.buttonWidth,
    GuiUtil.buttonHeight, TranslatableComponent("universeChat.rpmtw_platform_mod.gui.what"), {
        // TODO: rename `cosmic` to `universe`
        Util.openLink("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
    })