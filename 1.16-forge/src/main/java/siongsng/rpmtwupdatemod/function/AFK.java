package siongsng.rpmtwupdatemod.function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.Configer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 此類別部分原始碼取至:
 * https://github.com/littlejdude/AutoAFK/blob/master/AutoAFK/src/main/java/jdude/autoafk/Main.java
 */

public class AFK {

    public static final Map<UUID, PlayerInfo> PLAYER_INFOS = new HashMap<UUID, PlayerInfo>();

    @SubscribeEvent
    public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent connected) {
        if (!Configer.afk.get()) return;
        PLAYER_INFOS.put(connected.getPlayer().getGameProfile().getId(), new PlayerInfo((PlayerEntity) connected.getPlayer()));
    }

    @SubscribeEvent
    public void onPlayerDisconect(PlayerEvent.PlayerLoggedOutEvent disconnected) {
        if (!Configer.afk.get()) return;
        for (Entry<UUID, PlayerInfo> info : PLAYER_INFOS.entrySet()) {
            if (info.getKey() == disconnected.getPlayer().getGameProfile().getId()) {
                PLAYER_INFOS.remove(info.getKey());
                break;
            }
        }
    }

    @SubscribeEvent
    public void chatty(ServerChatEvent chat) {
        if (!Configer.afk.get()) return;
        PlayerInfo info = PLAYER_INFOS.get(chat.getPlayer().getGameProfile().getId());
        if (info == null) {
            return;
        }
        info.afkTime = 0;
        if (info.isAfk) {
            SendMsg.send("§a你不再掛機了!");
            info.isAfk = false;
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (!Configer.afk.get()) return;
        if (event.phase == TickEvent.Phase.START) {
            Iterator<Entry<UUID, PlayerInfo>> it = PLAYER_INFOS.entrySet().iterator();
            while (it.hasNext()) {
                Entry<UUID, PlayerInfo> info = it.next();
                if (info.getValue().player.getPosition().equals(info.getValue().position)) {
                    if (info.getValue().isAfk() && !info.getValue().isAfk) {
                        info.getValue().isAfk = true;
                        SendMsg.send("§6你開始掛機了! 因此開始檢測版本並下載新翻譯。");
                        new ReloadPack();
                    } else {
                        info.getValue().afkTime++;
                    }
                } else {
                    info.getValue().position = info.getValue().player.getPosition();
                    info.getValue().afkTime = 0;
                    if (info.getValue().isAfk) {
                        SendMsg.send("§a你不再掛機了!");
                        info.getValue().isAfk = false;
                    }
                }
            }
        }
    }
}
