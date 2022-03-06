package com.rpmtw.rpmtw_platform_mod.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Config(name = "rpmtw_platform_mod")
@Environment(EnvType.CLIENT)
class ConfigObject : ConfigData {
    @JvmField
    @ConfigEntry.Category("base")
    @ConfigEntry.Gui.TransitiveObject
    var base = Base()

    @JvmField
    @ConfigEntry.Category("translate")
    @ConfigEntry.Gui.TransitiveObject
    var translate = Translate()

    @JvmField
    @ConfigEntry.Category("cosmicChat")
    @ConfigEntry.Gui.TransitiveObject
    var cosmicChat = CosmicChat()


    class Base {
        @JvmField
        @ConfigEntry.Gui.Excluded
        var rpmtwAuthToken: String? = null

        fun isLogin(): Boolean {
            return rpmtwAuthToken != null && rpmtwAuthToken!!.isNotEmpty()
        }
    }

    class Translate {
        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var autoToggleLanguage = true
    }

    class CosmicChat {
        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var enable = true

        @JvmField
        var enableReceiveMessage = true

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        var enableButton = true

        @JvmField
        @ConfigEntry.Gui.Excluded
        var eula = false

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        var nickname: String? = null

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        var accountType: CosmicChatAccountType = CosmicChatAccountType.RPMTW
    }
}

enum class CosmicChatAccountType {
    MINECRAFT,
    RPMTW;

    val isMinecraft: Boolean
        get() = this == MINECRAFT

    val isRPMTW: Boolean
        get() = this == RPMTW
}