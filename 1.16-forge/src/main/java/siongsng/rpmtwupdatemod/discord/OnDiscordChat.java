package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class OnDiscordChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!Configer.discord.get()) return; //如果關閉宇宙通訊系統
        if (!e.getChannel().getId().equals("831494456913428501")) return; //如果不是該頻道則返回

        if (e.getMessage().getContentDisplay().startsWith(String.format("[宇宙通訊] **%s** >> ",
                Minecraft.getInstance().player.getDisplayName().getString())) ||
                (e.getMessage().getContentDisplay().startsWith(":clap_tone2: 熱烈掌聲www，讓我們一起歡迎 **")))
            return;

        SendMsg.send(String.format("§9[宇宙通訊] §c<§e%s§c> §f%s", e.getAuthor().getAsTag(), e.getMessage()));
    }
}
