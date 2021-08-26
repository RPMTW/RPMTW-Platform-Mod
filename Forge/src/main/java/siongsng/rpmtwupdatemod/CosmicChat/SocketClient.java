package siongsng.rpmtwupdatemod.CosmicChat;


import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SocketClient {
    private final Socket socket;

    {
        try {
            Minecraft mc = Minecraft.getInstance();
            User user = mc.getUser();
            Map<String, String> AuthMap = new HashMap<>();
            AuthMap.put("Token", user.getAccessToken());
            AuthMap.put("UUID", user.getUuid());

            IO.Options options = IO.Options.builder().setAuth(AuthMap).build();
            socket = IO.socket("https://api.rpmtwchat.ga", options).connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}