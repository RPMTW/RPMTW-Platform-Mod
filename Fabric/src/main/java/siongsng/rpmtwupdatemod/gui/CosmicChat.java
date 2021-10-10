package siongsng.rpmtwupdatemod.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.function.SendMsg;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CosmicChat extends LightweightGuiDescription {
    public CosmicChat() {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(300, 125);
        WLabel label = new WLabel(new LiteralText("宇宙通訊系統-發送訊息介面"), 0xFF5555);
        gui.add(label, (int) 5.8, 1, 2, 1);

        WButton Cancel = new WButton(new LiteralText("取消"));
        WButton Send = new WButton(new LiteralText("傳送"));
        WButton Info = new WButton(new LiteralText("這是什麼?"));
        gui.add(Cancel, 1, 5, 4, 2);
        gui.add(Send, 6, 5, 4, 2);
        gui.add(Info, 11, 5, 5, 2);

        WTextField Message = new WTextField(new LiteralText("請輸入要發送的訊息"));
        Message.setMaxLength(150);
        gui.add(Message, (int) 3.5,  3, 10, 2);

        Cancel.setOnClick(() -> MinecraftClient.getInstance().setScreen(null));
        Send.setOnClick(() -> {
            if (Message.getText().equals("")) {
                SendMsg.send("訊息不能是空的。");
            } else {
                SocketClient.Send(Message.getText());
            }
            MinecraftClient.getInstance().setScreen(null);
        });
        Info.setOnClick(() -> Util.getOperatingSystem().open("https://www.rpmtw.ga/Wiki/ModInfo#what-is-cosmic-system"));
        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png")));
    }
}
