package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new LiteralText(
                "§a歡迎使用 RPMTW Update Mod 萬用中文化模組\n§9如有問題可以到我們的Discord群組詢問\n§9Discord: https://discord.gg/5xApZtgV2u\n§9如果有些功能不需要也可以打開選單關閉(快捷鍵 O)"), false);
    }
}