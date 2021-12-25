package siongsng.rpmtwupdatemod.utilities;

import net.minecraft.client.MinecraftClient;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.gui.CosmicChat;
import siongsng.rpmtwupdatemod.gui.EULA;
import siongsng.rpmtwupdatemod.gui.Screen;

public class Utility {
    public static void openCosmicChatScreen(String initMessage) {
        if (RPMTWConfig.getConfig().isEULA) {
            CosmicChat.open(initMessage);
        } else {
            MinecraftClient.getInstance().openScreen(new Screen(new EULA(initMessage)));
        }
    }
}
