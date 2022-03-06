package com.rpmtw.rpmtw_platform_mod.command

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler

class LoginRPMTWAccountCommand {
    init {
        RPMTWPlatformModPlugin.registerClientCommand("rpmtw", "login") {
            RPMTWAuthHandler.login()
            return@registerClientCommand RPMTWCommand.success
        }
    }
}