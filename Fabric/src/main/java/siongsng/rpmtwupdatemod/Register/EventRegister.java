package siongsng.rpmtwupdatemod.Register;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.function.SendMsg;


@Environment(EnvType.CLIENT)
public class EventRegister {
	
	public static void init() {
		
		ClientLifecycleEvents.CLIENT_STOPPING.register(client ->{
			SocketClient.Disconnect();//關閉socket 釋放資源
		});
		
		
	}

}