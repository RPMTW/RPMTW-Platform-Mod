package com.rpmtw.rpmtw_platform_mod.mixins;

import com.rpmtw.rpmtw_platform_mod.translation.resourcepack.TranslateResourcePack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Pack.class)
public class FixPackCompatibility {
    @Shadow
    @Final
    private String id;

    /**
     * Fix the compatibility of the translation resource pack.
     * <p>
     * Because the translation pack is for 1.19.x, but the pack format in 1.19 ~ 1.19.2 is 9, and the pack format in 1.19.3+ is 12.
     * So we need to change the compatibility of the translation pack to {@link PackCompatibility#COMPATIBLE}.
     */
    @Inject(method = "getCompatibility", at = @At(value = "HEAD"), cancellable = true)
    public void fixPackCompatibility(CallbackInfoReturnable<PackCompatibility> cir) {
        String packId = TranslateResourcePack.INSTANCE.getPackId();
        if (cir.isCancelled() || !id.equals(packId)) return;

        cir.setReturnValue(PackCompatibility.COMPATIBLE);
    }
}
