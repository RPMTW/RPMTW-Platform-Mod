package siongsng.rpmtwupdatemod.utilities;

import net.minecraft.client.Minecraft;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.gui.CosmicChatScreen;
import siongsng.rpmtwupdatemod.gui.EULAScreen;

public class Utility {
    public static void openCosmicChatScreen(String initMessage) {
        if (RPMTWConfig.isEULA) {
            Minecraft.getMinecraft().displayGuiScreen(new CosmicChatScreen());
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new EULAScreen(initMessage));
        }
    }
}
