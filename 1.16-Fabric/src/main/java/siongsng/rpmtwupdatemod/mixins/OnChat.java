package siongsng.rpmtwupdatemod.mixins;

import me.shedaniel.autoconfig.AutoConfig;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.config.DiscordPrefix;
import siongsng.rpmtwupdatemod.discord.Chat;
import siongsng.rpmtwupdatemod.function.CheckChat;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.config.ConfigScreen;

@Mixin(ClientPlayerEntity.class)
public class OnChat {
    int times = 0;

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void onChat(String message, CallbackInfo info) {
        ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
        if (message.equals(DiscordPrefix.Prefix()) || !message.startsWith(DiscordPrefix.Prefix()) || !config.discord)
            return;//關閉宇宙通訊系統
        if (!new CheckChat().get()) {
            SendMsg.send("§c您目前的§bRPMTW繁化更新模組§c版本過舊，因此無法使用§9宇宙通訊§c功能。");
            return;
        }
        times++;
        TextChannel textChannel = Chat.bot.getTextChannelById("831494456913428501");
        if (message.contains("OAO") || message.contains("oao")) {
            message = message.replace("OAO", "<:OAO:827917219312828457>").replace("oao", "<:OAO:827917219312828457>");
        }
        textChannel.sendMessage(String.format("[宇宙通訊] **%s** >> %s", MinecraftClient.getInstance().player.getDisplayName().getString(), DiscordPrefix.get(message))).queue();
        if (times == 1 && new CheckChat().get()) {
            SendMsg.send("§b提醒您使用§9宇宙通訊§b功能需遵守§cDiscord使用者及社群條款\n" +
                    "§b以及§c不得以任何形式騷擾別人§b，違反者皆可能須附上§c法律責任§b。\n" +
                    "§b另外，若使用此功能功能，即代表您同意公開您的§cMinecraftID§b。");
        }
        info.cancel();
        SendMsg.send(String.format("§9[宇宙通訊] §c§l%s §b>> §f%s", MinecraftClient.getInstance().player.getDisplayName().getString(), DiscordPrefix.get(message)));
    }
}