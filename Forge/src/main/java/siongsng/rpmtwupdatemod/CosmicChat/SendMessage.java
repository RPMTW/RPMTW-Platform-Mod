package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class SendMessage {
    public void Send(String Message) {
        try {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            User user = mc.getUser();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("MessageType", "General");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", user.getName());

            player.displayClientMessage(new TextComponent("訊息發送中..."), true);
            new SocketClient().getSocket().emit("message", Data.toString());
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }
}