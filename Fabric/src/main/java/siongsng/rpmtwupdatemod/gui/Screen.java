package siongsng.rpmtwupdatemod.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class Screen extends CottonClientScreen {
    public Screen(GuiDescription description) {
        super(description);
        description.addPainters();
    }
}