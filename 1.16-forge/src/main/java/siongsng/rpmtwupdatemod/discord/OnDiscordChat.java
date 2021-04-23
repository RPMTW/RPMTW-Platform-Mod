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
        String AsTag = e.getAuthor().getAsTag();
        if (AsTag.equals("菘菘#8663")) {
            AsTag = "RPMTW維護者";
        }

        String msg = String.format("§9[宇宙通訊] §c§l%s §b>> §f%s", AsTag, e.getMessage());
        if (e.getAuthor().getId().equals("830747827567198248") && e.getMessage().getContentDisplay().startsWith("[宇宙通訊] **")) {
            msg = "§9[宇宙通訊] §c§l" + e.getMessage().getContentDisplay().split("\\*\\*")[1] + " §b>> §f" + e.getMessage().getContentDisplay().split("\\*\\*")[2].split(">> ")[1];
        }
        SendMsg.send(msg);
    }
}
