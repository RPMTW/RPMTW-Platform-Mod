package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public class ChatScreenAccessor {
    @Mixin(ChatScreen.class)
    public interface chatFieldAccessor {
        @Accessor(value = "inputField",remap = false)
        TextFieldWidget getChatField();
    }

}
