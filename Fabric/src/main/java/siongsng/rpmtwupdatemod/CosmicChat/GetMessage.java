package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.PlayerEntity;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class GetMessage {
    public GetMessage() {
        new SocketClient().getSocket().on(("broadcast"), (data) -> {
            try {
                if (MinecraftClient.getInstance() == null) return;
                if (!Configer.config.isChat) return;

                MinecraftClient mc = MinecraftClient.getInstance();
                PlayerEntity player = mc.player;
                Session session = mc.getSession();
                assert player != null;

                JsonParser jp = new JsonParser();
                JsonObject JsonData = (JsonObject) jp.parse(data[0].toString());
                String Type = JsonData.getAsJsonPrimitive("Type").getAsString();
                String MessageType = JsonData.getAsJsonPrimitive("MessageType").getAsString();

                switch (Type) {
                    case "Client":
                        if (MessageType.equals("General")) { //一般類型的訊息
                            String UserName = JsonData.getAsJsonPrimitive("UserName").getAsString();
                            String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
                            if (UserName.equals("菘菘#8663") || UserName.equals("SiongSng")) {
                                UserName = "§bRPMTW維護者";
                            }
                            SendMsg.send(String.format(("§9[宇宙通訊] §e<§6%s§e> §f%s"), UserName, Message));
                        }
                    case "Server":
                        switch (MessageType) {
                            case "Ban": {
                                String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                if (session.getProfile().getId().toString().equals(UUID)) {
                                    SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                }
                            }
                            break;
                            case "Auth": {
                                String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                if (session.getProfile().getId().toString().equals(UUID)) {
                                    SendMsg.send("由於您的 Minecraft 帳號不是正版，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                }
                            }
                            break;
                            case "Notice": {
                                String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
                                SendMsg.send(String.format(("§c[官方公告] §f%s"), Message));
                            }
                        }
                }

            } catch (Exception err) {
                RpmtwUpdateMod.LOGGER.warn("接收宇宙通訊訊息時發生未知錯誤，原因: " + err);
            }
        });

    }
}
