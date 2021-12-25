package siongsng.rpmtwupdatemod.gui.widget;


public class TranslucentButton extends RPMButton {

    public TranslucentButton(int x, int y, int width, int height, String message, RPMButton.IPressable onPress, String tooltip) {
        super(x, y, width, height, message, onPress, tooltip);
    }

    @Override
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        drawRect(x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}
