package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new LiteralText(noticeGet.get().replace("\\n", "\n")), false);
    }
}