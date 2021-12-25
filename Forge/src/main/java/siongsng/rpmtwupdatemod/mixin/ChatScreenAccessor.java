package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public class ChatScreenAccessor {
    @Mixin(ChatScreen.class)
    public interface chatFieldAccessor {
        @Accessor("input")
        EditBox getChatField();
    }

}
