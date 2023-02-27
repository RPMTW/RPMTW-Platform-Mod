package com.rpmtw.rpmtw_platform_mod.mixins;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.translation.machineTranslation.MTStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    void onReloadLanguage(ResourceManager resourceManager, CallbackInfo ci) {
        RPMTWPlatformMod.LOGGER.info("Reloading language...");
        MTStorage.INSTANCE.load(resourceManager);
    }
}
