package siongsng.rpmtwupdatemod.notice;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class notice {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        e.player.sendMessage(new TextComponentString("§a歡迎使用 RPMTW Update Mod 萬用中文化模組\n§f如有問題可以到我們的Discord群組詢問\n§fDiscord: https://discord.gg/5xApZtgV2u\n§f如果有些功能不需要也可以打開選單關閉(快捷鍵 O)"));
    }
}