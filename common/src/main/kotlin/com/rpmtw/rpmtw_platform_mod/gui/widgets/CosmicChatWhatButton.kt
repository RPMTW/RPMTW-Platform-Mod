package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_platform_mod.gui.CosmicChatScreen
import net.minecraft.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.TranslatableComponent

class CosmicChatWhatButton(width: Int, height: Int) : Button(
    width / 2 + 50, height / 2 + 30, 95,
    CosmicChatScreen.BUTTON_HEIGHT, TranslatableComponent("cosmicChat.rpmtw_platform_mod.gui.what"), {
        Util.getPlatform().openUri("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system")
    })