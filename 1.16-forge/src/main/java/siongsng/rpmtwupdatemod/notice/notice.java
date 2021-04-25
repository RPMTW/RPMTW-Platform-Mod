package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.CheckChat;

public class notice {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new TranslationTextComponent(
                "gui.notice"), p.getUniqueID());

        if (!new CheckChat().get()) {
            p.sendMessage(new StringTextComponent(
                    "偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。"), p.getUniqueID());
        }
    }
}
