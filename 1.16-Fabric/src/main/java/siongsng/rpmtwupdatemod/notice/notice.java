package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import siongsng.rpmtwupdatemod.function.CheckChat;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new TranslatableText(
                "gui.notice"), false);

        if (!new CheckChat().get()) {
            p.sendMessage(new LiteralText(
                    "偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。"), false);
        }
    }
}