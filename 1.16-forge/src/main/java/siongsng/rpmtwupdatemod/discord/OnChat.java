package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnChat {
    int times = 0;

    @SubscribeEvent
    public void onChat(ClientChatEvent e) {
        if (e.getMessage().equals("!")) return;
        if (e.getMessage().startsWith("!")) {
            times++;
            TextChannel textChannel = Chat.bot.getTextChannelById(815819581440262146L);
            textChannel.sendMessage(String.format("**%s** >> %s", Minecraft.getInstance().player.getDisplayName().getString(), e.getMessage().split("^!")[1])).queue();
        }
        if (e.getMessage().startsWith("!") && times == 1) {
            SendMsg.send("§b提醒您使用本功能(RPMTW Discord串流聊天)需遵守§cDiscord使用者及社群條款§b，還有§c不得以任何形式騷擾別人§b，如違反皆須附上§c法律責任§b。");
        }
    }
}