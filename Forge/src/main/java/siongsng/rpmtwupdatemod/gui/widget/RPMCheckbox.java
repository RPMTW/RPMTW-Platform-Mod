package siongsng.rpmtwupdatemod.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class RPMCheckbox extends Checkbox {
    protected final PressAction onPress;
    protected final String tooltip;

    public RPMCheckbox(int x, int y, int width, int height, TextComponent message, boolean checked, PressAction onPress, String tooltip) {
        this(x, y, width, height, message, checked, true, onPress, tooltip);
    }

    public RPMCheckbox(int x, int y, int width, int height, TextComponent message, boolean checked, PressAction onPress) {
        this(x, y, width, height, message, checked, true, onPress, "");
    }

    public RPMCheckbox(int x, int y, int width, int height, TextComponent message, boolean checked, boolean showMessage, PressAction onPress, String tooltip) {
        super(x, y, width, height, message, checked, showMessage);
        this.onPress = onPress;
        this.tooltip = tooltip;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(this.selected());
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        if (this.isHovered) {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.screen;
            if (screen != null) {
                screen.renderTooltip(matrices, new TextComponent(tooltip), mouseX, mouseY);
            }
        }
    }

    public interface PressAction {
        void onPress(boolean checked);
    }
}
