package com.rpmtw.rpmtw_platform_mod.config

import com.mojang.blaze3d.platform.InputConstants
import com.rpmtw.rpmtw_platform_mod.translation.GameLanguage
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Config(name = "rpmtw_platform_mod")
@Environment(EnvType.CLIENT)
class ConfigObject : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    var translate = Translate()

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    var universeChat = UniverseChat()

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    val keyBindings = KeyBindings()

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    val advanced = Advanced()

    class Translate {
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var loadTranslateResourcePack = GameLanguage.getSystem() == GameLanguage.TraditionalChinese

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var machineTranslation = true

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var autoMachineTranslation = false

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var unlocalized = true
            get() {
                return if (GameLanguage.getMinecraft() == GameLanguage.English) {
                    false
                } else {
                    field
                }
            }


        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var autoToggleLanguage = true
    }

    class UniverseChat {
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var enable = true

        var enableReceiveMessage = true

        @ConfigEntry.Gui.Tooltip(count = 1)
        var enableButton = true

        @ConfigEntry.Gui.Excluded
        var eula = false

        @ConfigEntry.Gui.Tooltip(count = 1)
        var nickname: String? = null

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        var accountType: UniverseChatAccountType = UniverseChatAccountType.MINECRAFT

        @ConfigEntry.Gui.Tooltip(count = 1)
        var blockUsers: MutableList<String> = mutableListOf()
    }

    class KeyBindings {
        var machineTranslation: ModifierKeyCode = ModifierKeyCode.unknown()

        // Ctrl + R
        var config: ModifierKeyCode = ModifierKeyCode.of(
            InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_R), Modifier.of(false, true, false)
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        var reloadTranslatePack: ModifierKeyCode = ModifierKeyCode.unknown()

        @ConfigEntry.Gui.Tooltip(count = 1)
        var openCrowdinPage: ModifierKeyCode = ModifierKeyCode.unknown()
    }

    class Advanced {
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var sendExceptionToSentry = true
    }

    @ConfigEntry.Gui.Excluded
    var protocolVersion : Int? = null

    @ConfigEntry.Gui.Excluded
    var rpmtwAuthToken: String? = null

    @ConfigEntry.Gui.Excluded
    var firstJoinLevel: Boolean = true

    fun isLogin(): Boolean {
        return rpmtwAuthToken != null && rpmtwAuthToken!!.isNotEmpty()
    }
}

enum class UniverseChatAccountType {
    MINECRAFT,
    RPMTW
}