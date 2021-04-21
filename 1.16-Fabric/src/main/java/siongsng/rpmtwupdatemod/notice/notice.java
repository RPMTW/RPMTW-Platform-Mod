package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new TranslatableText(
                "gui.notice"), false);
    }
}