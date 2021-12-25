package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Session;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
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
            Minecraft mc = Minecraft.getMinecraft();
            Session user = mc.getSession();

            Map<String, String> AuthMap = new HashMap<>();
            String Token = user.getToken();
            String UUID = user.getProfile().getId().toString();
            AuthMap.put("Token", Token);
            AuthMap.put("UUID", UUID);

            IO.Options options = IO.Options.builder().setAuth(AuthMap).build();
            socket = IO.socket("https://api.rpmtwchat.ga", options).connect();
        } catch (URISyntaxException e) {
            RpmtwUpdateMod.LOGGER.error(e);
        }
    }

    public static void GetMessage() {
        if (socket == null) {
            init();
        }

        socket.on(("broadcast"), (data) -> {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.player;

                if (player != null) {
                    Session user = mc.getSession();

                    JsonObject JsonData = (JsonObject) new JsonParser().parse(data[0].toString());
                    String Type = JsonData.getAsJsonPrimitive("Type").getAsString();
                    String MessageType = JsonData.getAsJsonPrimitive("MessageType").getAsString();

                    switch (Type) {
                        case "Client":
                            if (!RPMTWConfig.cosmicChat)
                                return;

                            if (MessageType.equals("General")) { // 一般類型的訊息
                                String UserName = JsonData.getAsJsonPrimitive("UserName").getAsString();
                                String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
                                if (UserName.equals("菘菘#8663") || UserName.equals("SiongSng")) {
                                    UserName = "§bRPMTW維護者";
                                }
                                TextComponentString text = new TextComponentString("").createCopy();
                                text.appendSibling(new TextComponentString("§9[宇宙通訊] ").setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("宇宙通訊系統是一個可在遊戲內外聊天的系統，由 RPMTW 萬用中文化模組提供此功能，可在聊天視窗中使用本系統。")))));
                                text.appendSibling(new TextComponentString(String.format(("§e<§6%s§e> §f%s"), UserName, Message)));
                                player.sendStatusMessage(text, false);
                            }

                        case "Server":
                            switch (MessageType) {
                                case "Ban":
                                    if (!RPMTWConfig.cosmicChat)
                                        return;
                                    if (user.getProfile().getId().toString().equals(JsonData.getAsJsonPrimitive("UUID").getAsString())) {
                                        SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                    }

                                case "Auth":
                                    if (!RPMTWConfig.cosmicChat)
                                        return;

                                    String UUID = JsonData.getAsJsonPrimitive("UUID").getAsString();
                                    if (user.getProfile().getId().toString().equals(UUID)) {
                                        SendMsg.send("由於您的 Minecraft 帳號不是正版，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                                    }

                                case "Notice":
                                    String Message = JsonData.getAsJsonPrimitive("Message").getAsString();
                                    SendMsg.send(String.format(("§c[RPMTW 官方公告] §f%s"), Message));

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
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            Session user = mc.getSession();
            assert player != null;

            JsonObject Data = new JsonObject();
            Data.addProperty("Type", "Client");
            Data.addProperty("MessageType", "General");
            Data.addProperty("Message", Message);
            Data.addProperty("UserName", user.getUsername());

            player.sendStatusMessage(new TextComponentString("訊息發送中..."), true);
            socket.emit("message", Data.toString());
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }

    public static void disconnect() {
        if (socket != null)
            socket.disconnect();

        socket = null;
        RpmtwUpdateMod.LOGGER.info("已中斷宇宙通訊的連線");
    }

    public Socket getSocket() {
        if (socket == null)
            init();

        return socket;
    }

}
