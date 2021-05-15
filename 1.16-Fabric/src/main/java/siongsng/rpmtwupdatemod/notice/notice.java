package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import siongsng.rpmtwupdatemod.function.CheckModVersion;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new TranslatableText(
                "gui.notice"), false);

        if (!new CheckModVersion().get()) {
            SendMsg.send("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: " + new CheckModVersion().ver + " 最新版本: " + new CheckModVersion().NewVer() + "\n下載連結:https://bit.ly/33MpXu8");
        }
    }
}