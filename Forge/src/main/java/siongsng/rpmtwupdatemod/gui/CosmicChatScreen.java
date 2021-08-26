/*
特別感謝 3X0DUS - ChAoS#6969 解惑關於此類別的一些問題。
 */
package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import siongsng.rpmtwupdatemod.CosmicChat.SendMessage;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class CosmicChatScreen extends GuiScreen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    GuiTextField Message;
    GuiButton Info;
    GuiButton Send;
    GuiButton Close;

    @Override
    public void initGui() {
        super.initGui();
        Message = new GuiTextField(1, fontRenderer, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20);
        Info = new GuiButton(
                1,
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "這是什麼?");

        Send = new GuiButton(
                2,
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "傳送");

        Close = new GuiButton(
                3,
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "取消");

        this.buttonList.add(Info);
        this.buttonList.add(Send);
        this.buttonList.add(Close);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "宇宙通訊系統-發送訊息介面";
        String Text1 = "請在下方框格內輸入要向宇宙發送的訊息。";

        this.drawString(fontRenderer, Screen, (this.width / 2 - fontRenderer.getStringWidth(Screen) / 2), height - 65, 0xFF5555);
        this.drawString(fontRenderer, Text1, this.width / 2 - fontRenderer.getStringWidth(Text1) / 2, height - 50, TextColor);

        Message.setMaxStringLength(200);
        Message.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == Info) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.rpmtw.ga/Wiki/ModInfo#what-is-cosmic-system"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (button == Send) {
            if (Message.getText().equals("")) {
                SendMsg.send("訊息不能是空的。");
            } else {
                new SendMessage().Send(Message.getText());
            }
            Minecraft.getMinecraft().displayGuiScreen(null);
        }

        if (button == Close) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        Message.textboxKeyTyped(c, i);
    }

    @Override
    public void mouseClicked(int i, int j, int k) throws IOException {
        Message.mouseClicked(i, j, k);
        super.mouseClicked(i, j, k);
    }

    @Override
    public void updateScreen() {
        Message.updateCursorCounter();
        super.updateScreen();
    }
}