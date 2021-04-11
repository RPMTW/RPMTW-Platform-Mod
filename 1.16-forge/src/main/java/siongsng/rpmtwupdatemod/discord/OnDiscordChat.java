package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnDiscordChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannel().getId().equals("815819581440262146")) return;
        if (e.getAuthor().isBot()) return;
        SendMsg.send(String.format("§9[DC] §c<§e%s§c> §f%s", e.getAuthor().getAsTag(), e.getMessage()));
    }
}
