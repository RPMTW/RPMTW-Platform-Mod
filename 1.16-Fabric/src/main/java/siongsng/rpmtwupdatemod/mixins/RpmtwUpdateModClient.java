package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class RpmtwUpdateModClient {
    @Inject(method = "<init>",at=@At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci){
        MinecraftClient.getInstance().options.language = "zh_tw";
    }
}
