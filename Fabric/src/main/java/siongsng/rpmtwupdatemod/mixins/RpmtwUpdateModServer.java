package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.AFK;

@Mixin(MinecraftServer.class)
public class RpmtwUpdateModServer {
    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {  //伺服器Tick事件
        if (RPMTWConfig.config.afk) { //讀取Config是否開啟掛機模式
            AFK.tickAfkStatus();
        }
    }
}