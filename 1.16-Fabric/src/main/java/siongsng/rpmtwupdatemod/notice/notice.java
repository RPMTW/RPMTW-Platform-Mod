package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class notice {
    public static void send(PlayerEntity p) {
        try {
            p.sendMessage(new LiteralText(noticeGet.get().replace("\\n", "\n")), false);
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("取得公告失敗，錯誤原因:" + e.getMessage());
        }
    }
}