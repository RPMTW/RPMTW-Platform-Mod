package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.platform.InputConstants
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@Config(name = "rpmtw_platform_mod")
@Environment(EnvType.CLIENT)
class ConfigObject : ConfigData {
    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    var base = Base()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    var translate = Translate()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    var cosmicChat = CosmicChat()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    val keyBindings = KeyBindings()

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

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var machineTranslation = true

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var unlocalized = Minecraft.getInstance().options.languageCode != "en_us"
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

    class KeyBindings {
        @JvmField
        var machineTranslation: ModifierKeyCode =
            ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_N), Modifier.none())
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