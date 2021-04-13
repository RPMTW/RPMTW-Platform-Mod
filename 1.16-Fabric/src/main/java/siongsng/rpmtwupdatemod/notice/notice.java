package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class notice {
    public static void send(PlayerEntity p) {
        try {
            p.sendMessage(new LiteralText("§0----------------------------------------------------\n" +
                    "§aRPMTW自動繁化更新模組-公告\n" +
                    "§9歡迎您使用本模組，如果遇到任何問題可以參考一下說明\n" +
                    "§b查看本模組功能: https://www.rpmtw.ga/Add-on/Update-mod\n" +
                    "§e官方Discord: https://discord.gg/5xApZtgV2u\n" +
                    "§e官方網站: https://www.rpmtw.ga\n" +
                    "§c目前本模組仍處於Bata狀態，但是已經新增許多功能，歡迎大家測試\n" +
                    "§0----------------------------------------------------\n"), false);
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("取得公告失敗，錯誤原因:" + e.getMessage());
        }
    }
}