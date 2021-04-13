package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnChat {
    int times = 0;

    @SubscribeEvent
    public void onChat(ClientChatEvent e) {
        String message = e.getMessage();

        if (message.equals("!")) return;
        if (message.startsWith("!")) {
            times++;
            TextChannel textChannel = Chat.bot.getTextChannelById(815819581440262146L);
            textChannel.sendMessage(String.format("[宇宙通訊] **%s** >> %s", Minecraft.getInstance().player.getDisplayName().getString(), message.split("^!")[1])).queue();
        }
        if (message.startsWith("!") && times == 1) {
            SendMsg.send("§b提醒您使用§9宇宙通訊§b功能需遵守§cDiscord使用者及社群條款\n§b以及§c不得以任何形式騷擾別人§b，違反者皆可能須附上§c法律責任§b。");
        }
    }
}