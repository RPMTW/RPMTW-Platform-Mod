package siongsng.rpmtwupdatemod.gui.widget;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class TranslucentButton extends Button {

    public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, ITextComponent message, Button.IPressable onPress) {
        super(xIn, yIn, widthIn, heightIn, message, onPress);
    }

    public TranslucentButton(int x, int y, int width, int height, ITextComponent message, Button.IPressable onPress, Button.ITooltip tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    @Override
    public void blit(MatrixStack stack, int x, int y, int textureX, int textureY, int width, int height) {
        fill(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}
