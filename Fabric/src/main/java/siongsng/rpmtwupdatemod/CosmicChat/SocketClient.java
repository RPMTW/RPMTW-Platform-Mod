package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.utilities.SendMsg;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SocketClient {
    private static Socket socket;

    public static void init() {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            Session session = mc.getSession();

            Map<String, String> AuthMap = new HashMap<>();
            String Token = session.getAccessToken();
            String UUID = session.getUuid();
            AuthMap.put("Token", Token);
            AuthMap.put("UUID", UUID);

            IO.Options options = IO.Options.builder().setAuth(AuthMap).build();
            socket = IO.socket("https://api.rpmtwchat.ga", options).connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void GetMessage() {
        if (socket == null) {
            init();
        }

        socket.on(("broadcast"), (data) -> {
            try {
                if (MinecraftClient.getInstance() == null)
                    return;
                if (!RPMTWConfig.getConfig().cosmicChat)
                    return;

                MinecraftClient mc = MinecraftClient.getInstance();
                PlayerEntity player = mc.player;

                if (player != null) {
                    Session session = mc.getSession();

                    JsonParser jp = new JsonParser();
                    JsonObject JsonData = (JsonObject) jp.parse(data[0].toString());
                    String Type = JsonData.getAsJsonPrimitive("Type").getAsString();
                    String MessageType = JsonData.getAsJsonPrimitive("MessageType").getAsString();

                    switch (Type) {
                        case "Client":
                            if (MessageType.equals("General")) { // 一般類型的訊息
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
                                    if (session.getUuid().equals(UUID)) {
                                        SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                    }
                                }
                                case "Auth" -> {
                                    String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                    if (session.getUuid().equals(UUID)) {
                                        SendMsg.send("由於您的 Minecraft 帳號不是正版，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                    }
                                }
                                case "Notice" -> {
                                    String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
                                    SendMsg.send(String.format(("§c[官方公告] §f%s"), Message));
                                }
                            }
                    }
                }

            } catch (Exception err) {
                RpmtwUpdateMod.LOGGER.warn("接收宇宙通訊訊息時發生未知錯誤，原因: " + err);
            }
        });

    }

    public static void sendMessage(String Message) {
        if (socket == null) {
            init();
        }
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity player = mc.player;
            Session session = mc.getSession();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("MessageType", "General");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", session.getUsername());

            player.sendMessage(new LiteralText("訊息發送中..."), true);
            socket.emit("message", Data.toString());

        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }

    public static void Disconnect() {
        if (socket != null)
            socket.disconnect();

        socket = null;
        RpmtwUpdateMod.LOGGER.info("已中斷宇宙通訊的連線");
    }

    public Socket getSocket() {
        if (socket == null) {
            init();
        }


        return socket;
    }

}
