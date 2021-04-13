package siongsng.rpmtwupdatemod.notice;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.discord.Chat;

public class notice {
    private static World world;

    @SubscribeEvent
    public void send(EntityJoinWorldEvent e) {
        Entity ep = e.getEntity();
        if (ep == Minecraft.getInstance().player && world != e.getWorld()) {
            ep.sendMessage(new StringTextComponent(
                    "§0----------------------------------------------------\n" +
                            "§aRPMTW自動繁化更新模組-公告\n" +
                            "§e歡迎您使用本模組，如果遇到任何問題可以參考一下說明\n" +
                            "§e官方Discord: https://discord.gg/5xApZtgV2u\n" +
                            "§e查看本模組功能: https://www.rpmtw.ga/Add-on/Update-mod\n" +
                            "§b如果有些功能不需要也可以打開設定選單關閉喔(預設快捷鍵為O)\n" +
                            "§0----------------------------------------------------"), ep.getUniqueID());
            world = e.getWorld();
            if (Configer.discord.get()) {
                Chat.bot.getTextChannelById("831494456913428501").sendMessage(String.format(":clap_tone2: 熱烈掌聲www，讓我們一起歡迎 **%s** 來到RPMTW的世界!", ep.getDisplayName().getString())).queue();
            }
        }
    }
}
