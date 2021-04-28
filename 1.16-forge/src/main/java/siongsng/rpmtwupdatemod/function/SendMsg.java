package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class SendMsg {
    public static void send(String msg) {
        PlayerEntity p = Minecraft.getInstance().player; //取得實例的玩家
        assert p != null;
        p.sendMessage(new StringTextComponent(msg), p.getUniqueID()); //發送訊息
    }
}
