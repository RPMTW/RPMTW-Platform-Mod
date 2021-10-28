package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

@Mixin(PlayerManager.class)
public class PlayJoinMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"), cancellable = false)
    private void playerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (RPMTWConfig.getConfig().notice) { //判斷Config
            player.sendMessage(new LiteralText(
                    "§a歡迎使用 RPMTW Update Mod 萬用中文化模組\n§f如有問題可以到我們的Discord群組詢問\n§fDiscord: https://discord.gg/5xApZtgV2u\n§f如果有些功能不需要也可以打開選單關閉(快捷鍵 O)"), false);
        }
    }
}
