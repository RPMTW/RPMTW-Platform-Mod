package siongsng.rpmtwupdatemod.CosmicChat;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SocketClient {
    private final Socket socket;

    {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            Session session = mc.getSession();

            Map<String, String> AuthMap = new HashMap<>();
            AuthMap.put("Token", session.getAccessToken());
            AuthMap.put("AccountType", session.getAccountType().name());
            AuthMap.put("UUID", session.getUuid());

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
