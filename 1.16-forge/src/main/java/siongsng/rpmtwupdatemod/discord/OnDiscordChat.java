package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnDiscordChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getChannel().getId().equals("815819581440262146")) return; //如果不是該頻道則返回

        if (e.getMessage().getContentDisplay().startsWith(String.format("[宇宙通訊] **%s** >> ",
                Minecraft.getInstance().player.getDisplayName().getString()))) return;

        SendMsg.send(String.format("§9[宇宙通訊] §c<§e%s§c> §f%s", e.getAuthor().getAsTag(), e.getMessage()));
    }
}