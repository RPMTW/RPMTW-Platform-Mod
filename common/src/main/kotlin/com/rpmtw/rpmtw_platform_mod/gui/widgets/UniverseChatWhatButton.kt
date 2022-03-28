package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreen
import net.minecraft.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.TranslatableComponent

class UniverseChatWhatButton(width: Int, height: Int) : Button(
    width / 2 + 50, height / 2 + 30, 95,
    UniverseChatScreen.BUTTON_HEIGHT, TranslatableComponent("universeChat.rpmtw_platform_mod.gui.what"), {
        // TODO: rename `cosmic` to `universe`
        Util.getPlatform().openUri("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
    })