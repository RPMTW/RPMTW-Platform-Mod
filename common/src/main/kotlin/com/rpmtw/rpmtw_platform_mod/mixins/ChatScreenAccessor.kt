@file:JvmName("ChatScreenAccessor")

package com.rpmtw.rpmtw_platform_mod.mixins

import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.ChatScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

class ChatScreenAccessor {
    @Mixin(ChatScreen::class)
    interface ChatFieldAccessor {
        @get:Accessor("input")
        val chatField: EditBox?
    }
}