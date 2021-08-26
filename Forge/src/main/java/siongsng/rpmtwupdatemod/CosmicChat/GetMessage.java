package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.player.LocalPlayer;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class GetMessage {
    public GetMessage() {
        new SocketClient().getSocket().on(("broadcast"), (data) -> {
            try {
                if (Minecraft.getInstance() == null) return;
                if (!RPMTWConfig.isChat.get()) return;

                Minecraft mc = Minecraft.getInstance();
                LocalPlayer player = mc.player;
                User user = mc.getUser();
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
                            case "Ban" -> {
                                String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                if (user.getUuid().equals(UUID)) {
                                    SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                }
                            }
                            case "Auth" -> {
                                String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                if (user.getUuid().equals(UUID)) {
                                    SendMsg.send("由於您的 Minecraft 帳號不是正版，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                }
                            }
                            case "Notice" -> {
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