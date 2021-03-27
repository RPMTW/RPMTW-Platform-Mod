package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;

public class GuiHandler {
    @SubscribeEvent
    public static void onGUI(GuiScreenEvent.InitGuiEvent.Post e) {
        if (e.getGui() instanceof IngameMenuScreen) {
            if (e.getGui() instanceof IngameMenuScreen) {
                boolean gmrmflag = ModList.get().isLoaded("gamemenuremovegfarb");
                Button options = (Button) e.getWidgetList().get(5);
                Button returnToMenu = (Button) e.getWidgetList().get(7);
                Button shareToLan = (Button) e.getWidgetList().get(6);
                if (shareToLan != null) {
                    e.addWidget(new Button(shareToLan.x, shareToLan.y + (gmrmflag ? 0 : 24), shareToLan.getWidth(), shareToLan.getHeight(), new TranslationTextComponent("menu.rpmtw"), (n) -> Minecraft.getInstance().displayGuiScreen(new ModListScreen(e.getGui()))));
                    shareToLan.x = e.getGui().width / 2 - 102;
                    shareToLan.setWidth(204);
                    if (gmrmflag)
                        shareToLan.y -= 24;
                }

                if (!gmrmflag) {
                    if (options != null)
                        options.y += 24;
                    if (returnToMenu != null)
                        returnToMenu.y += 24;
                    for (Widget widget : e.getWidgetList()) {
                        widget.y -= 16;
                    }
                }
            }
        }
    }
}
