package siongsng.rpmtwupdatemod.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class RPMCheckbox extends CheckboxWidget {
    protected final PressAction onPress;
    protected final String tooltip;

    public RPMCheckbox(int x, int y, int width, int height, Text message, boolean checked, PressAction onPress, String tooltip) {
        this(x, y, width, height, message, checked, true, onPress, tooltip);
    }

    public RPMCheckbox(int x, int y, int width, int height, Text message, boolean checked, PressAction onPress) {
        this(x, y, width, height, message, checked, true, onPress, "");
    }

    public RPMCheckbox(int x, int y, int width, int height, Text message, boolean checked, boolean showMessage, PressAction onPress, String tooltip) {
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
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        if (this.isHovered()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            Screen screen = mc.currentScreen;
            if (screen != null) {
                screen.renderTooltip(matrices, new LiteralText(tooltip), mouseX, mouseY);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(boolean checked);
    }
}
