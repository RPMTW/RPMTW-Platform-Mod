package siongsng.rpmtwupdatemod.function;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

public class onPlayerJoin {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        if (RPMTWConfig.notice.get()) {
            try {
                Entity p = e.getPlayer();
                assert p != null;
                p.sendMessage(new TextComponent(
                        "§a歡迎使用 RPMTW Update Mod 萬用中文化模組\n§f如有問題可以到我們的Discord群組詢問\n§fDiscord: https://discord.gg/5xApZtgV2u\n§f如果有些功能不需要也可以打開選單關閉(快捷鍵 O)"), p.getUUID());
                RPMTWConfig.notice.set(false);
            } catch (Exception ignored) {
            }
        }

    }
}