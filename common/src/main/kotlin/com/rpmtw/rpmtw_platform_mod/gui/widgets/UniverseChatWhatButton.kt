package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_platform_mod.gui.UniverseChatScreen
import com.rpmtw.rpmtw_platform_mod.util.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class UniverseChatWhatButton(width: Int, height: Int) : Button(
    width / 2 + 50, height / 2 + 30, 95,
    UniverseChatScreen.BUTTON_HEIGHT, Component.translatable("universeChat.rpmtw_platform_mod.gui.what"), {
        // TODO: rename `cosmic` to `universe`
        Util.openLink("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
    })