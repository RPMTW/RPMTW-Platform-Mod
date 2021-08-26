package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Session;
import net.minecraft.util.text.StringTextComponent;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class SendMessage {
    public void Send(String Message) {
        try {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            Session session = mc.getSession();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("MessageType", "General");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", session.getUsername());

            player.sendStatusMessage(new StringTextComponent("訊息發送中..."), true);
            new SocketClient().getSocket().emit("message", Data.toString());
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }
}
