package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class GetMessage {
    public GetMessage() {
        new SocketClient().getSocket().connect().on(("broadcast"), (data) -> {
            if (MinecraftClient.getInstance() == null) return;
            JsonParser jp = new JsonParser();
            JsonObject JsonData = (JsonObject) jp.parse(data[0].toString());
            String Type = JsonData.getAsJsonPrimitive("Type").getAsString();
            String UserName = JsonData.getAsJsonPrimitive("UserName").getAsString();
            String IP = JsonData.getAsJsonPrimitive("IP").getAsString();
            String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
            if (Type.equals("Client")) { //來自客戶端的訊息
                if (UserName.equals("菘菘#8663")) {
                    UserName = "§bRPMTW維護者";
                }
                SendMsg.send(String.format(("§9[宇宙通訊] §e<§6%s§e> §f%s"), UserName, Message));
            } else if (Type.equals("Server")) { //來自伺服器端的訊息
                MinecraftClient mc = MinecraftClient.getInstance();
                PlayerEntity player = mc.player;
                assert player != null;
                if (new GetIP().Get().equals(IP) && Message.equals("Ban")) {
                    SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                }
            }
        });
    }
}
