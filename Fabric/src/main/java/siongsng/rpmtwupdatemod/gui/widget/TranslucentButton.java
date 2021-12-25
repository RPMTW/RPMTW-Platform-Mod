package siongsng.rpmtwupdatemod.gui.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TranslucentButton extends ButtonWidget {

    public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, Text message, ButtonWidget.PressAction onPress) {
        super(xIn, yIn, widthIn, heightIn, message, onPress);
    }

    public TranslucentButton(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    @Override
    public void drawTexture(MatrixStack stack, int x, int y, int textureX, int textureY, int width, int height) {
        fill(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}
