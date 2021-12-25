package siongsng.rpmtwupdatemod.gui.widget;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

public class RPMButton extends GuiButton {
    String tooltip;
    IPressable onPress;

    public RPMButton(int x, int y, int width, int height, String message, IPressable onPress, String tooltip) {
        super(0, x, y, width, height, message);
        this.onPress = onPress;
        this.tooltip = tooltip;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if (pressed){
            onPress.onPress();
        }
        return pressed;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        GuiScreen screen = mc.currentScreen;
        if (hovered && screen != null) {
            screen.drawHoveringText(tooltip, mouseX, mouseY);
        }
    }

    public interface IPressable {
        void onPress();
    }

}
