package siongsng.rpmtwupdatemod.notice;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.CheckModVersion;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class notice {
    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent.Post e) {
        Entity p = Minecraft.getMinecraft().player;
        assert p != null;
        p.sendMessage(new TextComponentTranslation("gui.notice"));

        if (!new CheckModVersion().get()) {
            SendMsg.send("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: " + new CheckModVersion().ver + " 最新版本: " + new CheckModVersion().NewVer() + "\n下載連結:https://bit.ly/33MpXu8");
        }
    }
}