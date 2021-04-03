package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.notice.notice;

@Mixin(PlayerManager.class)
public class PlayJoinMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"), cancellable = false)
    private void playerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        notice.send(player);
    }
}
