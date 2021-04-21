package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class notice {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new TranslationTextComponent(
                "gui.notice"), p.getUniqueID());
    }
}
