package siongsng.rpmtwupdatemod.gui.widget;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class TranslucentButton extends Button {

    public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, TextComponent message, Button.OnPress onPress) {
        super(xIn, yIn, widthIn, heightIn, message, onPress);
    }

    public TranslucentButton(int x, int y, int width, int height, TextComponent message, Button.OnPress onPress, Button.OnTooltip tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    @Override
    public void blit(PoseStack stack, int x, int y, int textureX, int textureY, int width, int height) {
        fill(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}