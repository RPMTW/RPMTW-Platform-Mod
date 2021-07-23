package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TextComponent;

public class SendMessage {
    public void Send(String Message) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        String IP = new GetIP().Get();
        assert player != null;
        JsonParser jp = new JsonParser();
        Message.replaceAll("\"","\\\"").replaceAll("\\",""); //跳脫字元處理
        String Data = String.format("{\"Type\":\"Client\",\"Message\":\"%s\",\"UserName\":\"%s\",\"UUID\":\"%s\",\"IP\":\"%s\"}", Message, player.getName().getString(), player.getUUID(), IP);
        player.displayClientMessage(new TextComponent("訊息發送中..."), true);
        new SocketClient().getSocket().connect().emit("message", jp.parse(Data));
    }
}