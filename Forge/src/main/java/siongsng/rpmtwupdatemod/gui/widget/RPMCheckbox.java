package siongsng.rpmtwupdatemod.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;


public class RPMCheckbox extends CheckboxButton {
    protected final PressAction onPress;
    protected final String tooltip;

    public RPMCheckbox(int x, int y, int width, int height, ITextComponent message, boolean checked, PressAction onPress, String tooltip) {
        this(x, y, width, height, message, checked, true, onPress, tooltip);
    }

    public RPMCheckbox(int x, int y, int width, int height, ITextComponent message, boolean checked, PressAction onPress) {
        this(x, y, width, height, message, checked, true, onPress, "");
    }

    public RPMCheckbox(int x, int y, int width, int height, ITextComponent message, boolean checked, boolean showMessage, PressAction onPress, String tooltip) {
        super(x, y, width, height, message, checked, showMessage);
        this.onPress = onPress;
        this.tooltip = tooltip;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(this.isChecked());
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderWidget(matrices, mouseX, mouseY, delta);
        if (this.isHovered) {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.currentScreen;
            if (screen != null) {
                screen.renderTooltip(matrices, new StringTextComponent(tooltip), mouseX, mouseY);
            }
        }
    }

    public interface PressAction {
        void onPress(boolean checked);
    }
}
