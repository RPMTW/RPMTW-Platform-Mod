package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.function.AFK;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public class InputMixin extends Input {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onInputTick(boolean slowDown, CallbackInfo ci) {
        if (pressingForward || pressingBack || pressingLeft || pressingRight || jumping || sneaking) {
            AFK.afkTime = 0;
        }
    }
}
