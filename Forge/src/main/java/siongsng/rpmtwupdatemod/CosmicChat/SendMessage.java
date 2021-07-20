package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class SendMessage {
    public void Send(String Message) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        String IP = new GetIP().Get();
        assert player != null;
        JsonParser jp = new JsonParser();
        String Data = String.format("{\"Type\":\"Client\",\"Message\":\"%s\",\"UserName\":\"%s\",\"UUID\":\"%s\",\"IP\":\"%s\"}", Message, player.getName(), player.getUniqueID(), IP);
        player.sendStatusMessage(new TextComponentString("訊息發送中..."), true);
        new SocketClient().getSocket().connect().emit("message", jp.parse(Data));
    }
}