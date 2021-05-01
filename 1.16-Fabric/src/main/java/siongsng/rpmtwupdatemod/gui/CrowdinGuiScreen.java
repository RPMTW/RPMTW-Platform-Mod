package siongsng.rpmtwupdatemod.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class CrowdinGuiScreen extends CottonClientScreen {
    public CrowdinGuiScreen(GuiDescription description) {
        super(description);
        description.addPainters();
    }
}