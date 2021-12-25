package siongsng.rpmtwupdatemod.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TextComponent;

public class SendMsg {
    public static void send(String msg) {
        try {
            Player p = Minecraft.getInstance().player; //取得實例的玩家
            assert p != null;
            p.sendMessage(new TextComponent(msg), p.getUUID()); //發送訊息
        } catch (Throwable ignored) {
        }
    }
}
