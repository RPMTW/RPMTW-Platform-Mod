package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class notice {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new StringTextComponent(
                "§0----------------------------------------------------\n" +
                        "§aRPMTW自動繁化更新模組-公告\n" +
                        "§e歡迎您使用本模組，如果遇到任何問題可以參考一下說明\n" +
                        "§e官方Discord: https://discord.gg/5xApZtgV2u\n" +
                        "§e查看本模組功能: https://www.rpmtw.ga/Add-on/Update-mod\n" +
                        "§b如果有些功能不需要也可以打開設定選單關閉喔(預設快捷鍵為O)\n" +
                        "§0----------------------------------------------------"), p.getUniqueID());
    }
}
