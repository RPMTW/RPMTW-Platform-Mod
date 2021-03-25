package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
    @Shadow
    private Set<ResourcePackProvider> providers;
    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerLoader(CallbackInfo info) {
        this.providers = new HashSet(this.providers);
        try {
            Class.forName("siongsng.rpmtwupdatemod.packs.LoadPack").getMethod("init", Set.class).invoke(null, this.providers);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}