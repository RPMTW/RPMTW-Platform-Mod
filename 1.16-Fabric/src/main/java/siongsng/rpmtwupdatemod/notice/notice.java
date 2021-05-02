package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import siongsng.rpmtwupdatemod.function.CheckChat;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class notice {
    public static void send(PlayerEntity p) {
        p.sendMessage(new TranslatableText(
                "gui.notice"), false);

        if (!new CheckChat().get()) {
            SendMsg.send("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: " + new CheckChat().ver + " 最新版本:" + new CheckChat().NewVer + "\n下載連結:https://www.curseforge.com/minecraft/mc-mods/rpmtw-update-mod");
        }
    }
}