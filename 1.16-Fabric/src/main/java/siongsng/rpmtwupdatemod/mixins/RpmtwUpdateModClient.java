package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.commands.noticeCMD;
import siongsng.rpmtwupdatemod.discord.Chat;

@Mixin(MinecraftClient.class)
public class RpmtwUpdateModClient {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci) {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> { //註冊指令
            noticeCMD.register(dispatcher); //獲取公告指令
        });
        new Chat();
        MinecraftClient.getInstance().options.language = "zh_tw";
    }
}
