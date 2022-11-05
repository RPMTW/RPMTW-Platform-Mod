package com.rpmtw.rpmtw_platform_mod.config

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import dev.architectury.platform.Platform
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class MixinConfigPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {
        RPMTWPlatformMod.LOGGER.info("Loaded mixin config plugin")
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        // Fix conflict with Chat Heads mod (https://github.com/dzwdz/chat_heads/blob/e9fad841f8bc39a9f9a10b821497e3ec3d853d2f/src/main/java/dzwdz/chat_heads/mixin/ChatComponentMixin.java#L83-L92)
        if (Platform.isModLoaded("chat_heads") && mixinClassName == "com.rpmtw.rpmtw_platform_mod.mixins.ChatComponentMixin") {
            return false
        }

        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {
        RPMTWPlatformMod.LOGGER.info("Accepting targets: $myTargets, $otherTargets")
    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}