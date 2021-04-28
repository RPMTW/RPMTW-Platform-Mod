package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.config.DiscordPrefix;
import siongsng.rpmtwupdatemod.function.CheckChat;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnChat {
    int times = 0;

    @SubscribeEvent
    public void onChat(ClientChatEvent e) {
        String message = e.getMessage();
        if (message.equals(DiscordPrefix.Prefix()) || !message.startsWith(DiscordPrefix.Prefix()) || !Configer.discord.get())
            return;//關閉宇宙通訊系統
        if (!new CheckChat().get()) { //如果版本過舊
            SendMsg.send("§c您目前的§bRPMTW繁化更新模組§c版本過舊，因此無法使用§9宇宙通訊§c功能。");
            return;
        }
        if (message.startsWith("/") && DiscordPrefix.get(message).equals("!")) return; //如果訊息開頭是 / 並且僅前綴發送訊息關閉
        times++;
        TextChannel textChannel = Chat.bot.getTextChannelById("831494456913428501");
        if (message.contains("OAO") || message.contains("oao")) {
            message = message.replace("OAO", "<:OAO:827917219312828457>").replace("oao", "<:OAO:827917219312828457>");
        }
        assert textChannel != null;
        assert Minecraft.getInstance().player != null;
        textChannel.sendMessage(String.format("[宇宙通訊] **%s** >> %s", Minecraft.getInstance().player.getDisplayName().getString(), DiscordPrefix.get(message))).queue();
        if (times == 1 && new CheckChat().get()) {
            SendMsg.send("§b提醒您使用§9宇宙通訊§b功能需遵守§cDiscord使用者及社群條款\n" +
                    "§b以及§c不得以任何形式騷擾別人§b，違反者皆可能須附上§c法律責任§b。\n" +
                    "§b另外，若使用此功能功能，即代表您同意公開您的§cMinecraftID§b。");
        }
        e.setCanceled(true);
        SendMsg.send(String.format("§9[宇宙通訊] §c§l%s §b>> §f%s", Minecraft.getInstance().player.getDisplayName().getString(), DiscordPrefix.get(message)));
    }
}