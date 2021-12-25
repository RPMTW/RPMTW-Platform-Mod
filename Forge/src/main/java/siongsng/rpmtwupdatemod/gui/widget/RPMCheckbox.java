package siongsng.rpmtwupdatemod.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiCheckBox;


public class RPMCheckbox extends GuiCheckBox {
    protected final PressAction onPress;
    protected final String tooltip;

    public RPMCheckbox(int x, int y, String message, boolean checked, PressAction onPress) {
        this(x, y, message, checked, onPress, "");
    }

    public RPMCheckbox(int x, int y, String message, boolean checked, PressAction onPress, String tooltip) {
        super(0, x, y, message, checked);
        this.onPress = onPress;
        this.tooltip = tooltip;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if (pressed) {
            onPress.onPress(this.isChecked());
        }
        return pressed;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        super.drawButton(mc, mouseX, mouseY, partial);
        if (hovered) {
            GuiScreen screen = mc.currentScreen;
            if (screen != null) {
                screen.drawHoveringText(tooltip, mouseX, mouseY);
            }
        }
    }

    public interface PressAction {
        void onPress(boolean checked);
    }
}
