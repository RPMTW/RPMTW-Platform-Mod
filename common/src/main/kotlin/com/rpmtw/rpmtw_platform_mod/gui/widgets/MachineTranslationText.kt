package com.rpmtw.rpmtw_platform_mod.gui.widgets

import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTManager
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

class MachineTranslationText(source: String, vararg i18nArgs: Any? = arrayOf()) : Component {
    private val text: MutableComponent

    init {
        text = MTManager.create(source, *i18nArgs)
    }

    override fun getStyle(): Style {
        return text.style
    }

    override fun getContents(): ComponentContents {
        return text.contents
    }

    override fun getSiblings(): MutableList<Component> {
        return text.siblings
    }

    override fun plainCopy(): MutableComponent {
        return text.plainCopy()
    }

    override fun copy(): MutableComponent {
        return text.copy()
    }

    override fun getVisualOrderText(): FormattedCharSequence {
        return text.visualOrderText
    }
}