package siongsng.rpmtwupdatemod.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class SendMsg {
    public static void send(String msg) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        player.sendMessage(new TextComponentString(msg));
    }
}
