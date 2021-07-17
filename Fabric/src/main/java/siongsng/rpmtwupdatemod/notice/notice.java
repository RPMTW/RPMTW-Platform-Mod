package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import siongsng.rpmtwupdatemod.function.CheckModVersion;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new TranslatableText(
                "gui.notice"), false);

        if (!new CheckModVersion().get()) {
            p.sendMessage(new LiteralText("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: "
                    + new CheckModVersion().ver + " 最新版本: "
                    + new CheckModVersion().NewVer() + "\n下載連結:https://www.rpmtw.ga\n變更日誌: "
                    + new CheckModVersion().ChangeLog()), false);
        }
    }
}