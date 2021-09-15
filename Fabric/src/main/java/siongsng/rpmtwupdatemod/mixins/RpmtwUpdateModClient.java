package siongsng.rpmtwupdatemod.mixins;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.resource.language.LanguageDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.commands.noticeCMD;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

@Mixin(MinecraftClient.class)
public class RpmtwUpdateModClient {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci) {
        if (RPMTWConfig.getConfig().isChinese) {
            MinecraftClient.getInstance().getLanguageManager().setLanguage(new LanguageDefinition("zh_tw", "TW", "繁體中文", false));
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> { //註冊指令
            noticeCMD.register(dispatcher); //獲取公告指令
        });
    }
}
