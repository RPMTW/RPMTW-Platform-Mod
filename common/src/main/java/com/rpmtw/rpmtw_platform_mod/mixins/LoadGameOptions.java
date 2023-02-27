package com.rpmtw.rpmtw_platform_mod.mixins;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class LoadGameOptions {
    private boolean called = false;

    @Inject(method = "load", at = @At("HEAD"))
    public void loading(CallbackInfo ci) {
        if (called) return;

        RPMTWPlatformMod.LOGGER.info("Loading the game options...");
        TranslateResourcePack.INSTANCE.init();
        called = true;
    }
}
