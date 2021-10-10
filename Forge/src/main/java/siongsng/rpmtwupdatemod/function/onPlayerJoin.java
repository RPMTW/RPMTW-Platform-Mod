package siongsng.rpmtwupdatemod.function;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class onPlayerJoin {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new TranslatableComponent(
                "gui.notice"), p.getUUID());
        if (!new CheckModVersion().get()) {
            p.sendMessage(new TextComponent("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: "
                    + new CheckModVersion().ver + " 最新版本: "
                    + new CheckModVersion().NewVer() + "\n下載連結:https://www.rpmtw.ga\n變更日誌: "
                    + new CheckModVersion().ChangeLog()), p.getUUID());
        }
    }
}