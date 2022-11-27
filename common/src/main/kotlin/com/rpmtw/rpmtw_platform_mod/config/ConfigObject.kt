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
import org.lwjgl.glfw.GLFW

@Config(name = "rpmtw_platform_mod")
@Environment(EnvType.CLIENT)
class ConfigObject : ConfigData {
    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    var base = Base()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    var translate = Translate()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    var universeChat = UniverseChat()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    val keyBindings = KeyBindings()

    @JvmField
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    val advanced = Advanced()

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
        var machineTranslation = true

        @JvmField
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
        var loadTranslateResourcePack = true
            get() {
                return if (GameLanguage.getMinecraft() == GameLanguage.English) {
                    false
                } else {
                    field
                }
            }
    }

    class UniverseChat {
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
        var accountType: UniverseChatAccountType = UniverseChatAccountType.MINECRAFT

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        var blockUsers: MutableList<String> = mutableListOf()
    }

    class KeyBindings {
        @JvmField
        var machineTranslation: ModifierKeyCode = ModifierKeyCode.unknown()

        // Ctrl + R
        @JvmField
        var config: ModifierKeyCode = ModifierKeyCode.of(
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_R), Modifier.of(false, true, false)
        )

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        var reloadTranslatePack: ModifierKeyCode = ModifierKeyCode.unknown()

        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        var openCrowdinPage: ModifierKeyCode = ModifierKeyCode.unknown()
    }

    class Advanced {
        @JvmField
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.Gui.RequiresRestart
        var sendExceptionToSentry = true
    }
}

enum class UniverseChatAccountType {
    MINECRAFT,
    RPMTW
}