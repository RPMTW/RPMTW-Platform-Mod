package siongsng.rpmtwupdatemod.Register;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;

@Environment(EnvType.CLIENT)
public class EventRegister {

    public static void init() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            SocketClient.disconnect();//關閉 socket 釋放資源
        });
    }

}