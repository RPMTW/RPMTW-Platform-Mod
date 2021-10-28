package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class notice {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        try {
            Entity p = e.getPlayer();
            assert p != null;
            p.sendMessage(new StringTextComponent(
                    "§a歡迎使用 RPMTW Update Mod 萬用中文化模組\n§9如有問題可以到我們的Discord群組詢問\n§9Discord: https://discord.gg/5xApZtgV2u\n§9如果有些功能不需要也可以打開選單關閉(快捷鍵 O)"), p.getUniqueID());
        } catch (Throwable ignored) {
        }
    }
}
