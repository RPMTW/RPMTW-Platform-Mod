package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class SendMessage {
    public void Send(String Message) {
        try {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            String IP = new GetIP().Get();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", player.getName().getString());
            Data.addProperty("UUID", player.getUUID().toString());
            Data.addProperty("IP", IP);

            player.displayClientMessage(new TextComponent("訊息發送中..."), true);
            new SocketClient().getSocket().connect().emit("message", Data);
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }
}