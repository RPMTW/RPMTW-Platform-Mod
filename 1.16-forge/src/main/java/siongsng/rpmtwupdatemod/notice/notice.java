package siongsng.rpmtwupdatemod.notice;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;

public class notice {
    private static World world;

    @SubscribeEvent
    public void send(EntityJoinWorldEvent e) {
        Entity en = e.getEntity();
        if (en == Minecraft.getInstance().player && world != e.getWorld()) {
            try {
                en.sendMessage(new StringTextComponent(noticeGet.get().replace("\\n", "\n")), en.getUniqueID());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            world = e.getWorld();
        }
    }
}
