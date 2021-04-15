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
                MinecraftClient.getInstance().player.getDisplayName().getString())))
            return;

        String msg = String.format("§9[宇宙通訊] §c§l%s §b>> §f%s", e.getAuthor().getAsTag(), e.getMessage());
        if (e.getAuthor().getId().equals("830747827567198248") && e.getMessage().getContentDisplay().startsWith("[宇宙通訊] **")) {
            msg = "§9[宇宙通訊] §c§l" + e.getMessage().getContentDisplay().split("\\*\\*")[1] + " §b>> §f" + e.getMessage().getContentDisplay().split("\\*\\*")[2].split(">> ")[1];
        }
        SendMsg.send(msg);
    }
}
