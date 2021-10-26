package siongsng.rpmtwupdatemod.function;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class onPlayerJoin {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new TextComponent(
                "§e歡迎使用 RPMTW Update Mod 萬用中文化模組\n§a如有問題可以到我們的Discord群組詢問\n§eDiscord: https://discord.gg/5xApZtgV2u\n§如果有些功能不需要也可以打開選單關閉(預設快捷鍵為O)"), p.getUUID());
    }
}