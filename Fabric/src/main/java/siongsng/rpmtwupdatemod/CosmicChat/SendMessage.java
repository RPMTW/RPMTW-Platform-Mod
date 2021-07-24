package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class SendMessage {
    public void Send(String Message) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity player = mc.player;
            String IP = new GetIP().Get();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", player.getName().getString());
            Data.addProperty("UUID", player.getUuidAsString());
            Data.addProperty("IP", IP);

            player.sendMessage(new LiteralText("訊息發送中..."), true);
            new SocketClient().getSocket().connect().emit("message", Data.toString());
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }
}
