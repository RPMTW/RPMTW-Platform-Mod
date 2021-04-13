package siongsng.rpmtwupdatemod.mixins;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.discord.Chat;
import siongsng.rpmtwupdatemod.gui.ConfigScreen;
import siongsng.rpmtwupdatemod.notice.notice;

@Mixin(PlayerManager.class)
public class PlayJoinMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"), cancellable = false)
    private void playerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
        if (config.notice) { //判斷Config
            notice.send(player);
        }
        if (config.discord) {
            Chat.bot.getTextChannelById("831494456913428501").sendMessage(String.format(":clap_tone2: 熱烈掌聲www，讓我們一起歡迎 **%s** 來到RPMTW的世界!", player.getDisplayName().getString())).queue();
        }
        ;
    }
}
