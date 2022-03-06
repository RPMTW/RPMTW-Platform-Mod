package com.rpmtw.rpmtw_platform_mod.command

import com.mojang.brigadier.Command
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object RPMTWCommand {
    const val success: Int = Command.SINGLE_SUCCESS

    fun handle() {
        ReplyCosmicMessageCommand()
        LoginRPMTWAccountCommand()
    }
}