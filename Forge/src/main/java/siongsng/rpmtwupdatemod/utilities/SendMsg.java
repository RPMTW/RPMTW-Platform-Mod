package siongsng.rpmtwupdatemod.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class SendMsg {
    public static void send(String msg) {
        try {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity p = mc.player; //取得實例的玩家
            assert p != null;
            p.sendMessage(new StringTextComponent(msg), p.getUniqueID()); //發送訊息
        } catch (Throwable ignored) {
        }
    }
}
