package siongsng.rpmtwupdatemod.CosmicChat;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketClient {
    private final Socket socket;

    {
        try {
            IO.Options options = IO.Options.builder()
                    .build();
            socket = IO.socket("https://Cosmic-Chat-Server.siongsng.repl.co", options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}