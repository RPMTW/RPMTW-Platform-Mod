package siongsng.rpmtwupdatemod.notice;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class notice {
    @SubscribeEvent
    public void send(EntityJoinWorldEvent e) {
        Entity en = e.getEntity();
        if (en == Minecraft.getInstance().player) {
            en.sendMessage(new StringTextComponent(noticeGet.get().replace("\\n", "\n")), en.getUniqueID());
        }
    }
}
