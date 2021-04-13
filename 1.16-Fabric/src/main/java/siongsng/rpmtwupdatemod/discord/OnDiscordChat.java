package siongsng.rpmtwupdatemod.discord;

import me.shedaniel.autoconfig.AutoConfig;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.MinecraftClient;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.ConfigScreen;

public class OnDiscordChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
        if (!config.discord) return;
        if (!e.getChannel().getId().equals("831494456913428501")) return; //如果不是該頻道則返回

        if (e.getMessage().getContentDisplay().startsWith(String.format("[宇宙通訊] **%s** >> ",
                MinecraftClient.getInstance().player.getDisplayName().getString())) ||
                (e.getMessage().getContentDisplay().startsWith(":clap_tone2: 熱烈掌聲www，讓我們一起歡迎 **")))
            return;

        SendMsg.send(String.format("§9[宇宙通訊] §c<§e%s§c> §f%s", e.getAuthor().getAsTag(), e.getMessage()));
    }
}
