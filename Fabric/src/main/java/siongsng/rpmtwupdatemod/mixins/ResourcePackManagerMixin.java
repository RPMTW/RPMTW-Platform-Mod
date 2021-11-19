package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.packs.PackManeger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
    @Mutable
    @Final
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void registerLoader(CallbackInfo info) throws IOException {

        this.providers = new HashSet<>(this.providers);
        PackManeger.PackVersionCheck(this.providers);
    }
}