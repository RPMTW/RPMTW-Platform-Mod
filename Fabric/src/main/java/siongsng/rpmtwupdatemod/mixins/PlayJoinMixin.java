package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.CheckModVersion;

@Mixin(PlayerManager.class)
public class PlayJoinMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"), cancellable = false)
    private void playerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (RPMTWConfig.config.notice) { //判斷Config
            player.sendMessage(new TranslatableText(
                    "gui.notice"), false);

            if (!new CheckModVersion().get()) {
                player.sendMessage(new LiteralText("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: "
                        + new CheckModVersion().ver + " 最新版本: "
                        + new CheckModVersion().NewVer() + "\n下載連結:https://www.rpmtw.ga\n變更日誌: "
                        + new CheckModVersion().ChangeLog()), false);
            }
        }
    }
}
