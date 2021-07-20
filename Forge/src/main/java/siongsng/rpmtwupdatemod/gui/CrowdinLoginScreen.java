/*
特別感謝 3X0DUS - ChAoS#6969 解惑關於此類別的一些問題。
 */
package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class CrowdinLoginScreen extends GuiScreen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    GuiTextField Token;
    GuiButton Info;
    GuiButton Login;
    GuiButton Close;

    @Override
    public void initGui() {
        super.initGui();
        Token = new GuiTextField(1, fontRenderer, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20);
        Info = new GuiButton(
                1,
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "查看帳號登入教學");

        Login = new GuiButton(
                2,
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "登入");

        Close = new GuiButton(
                3,
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "關閉");

        this.buttonList.add(Info);
        this.buttonList.add(Login);
        this.buttonList.add(Close);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "RPMTW X Crowdin 翻譯帳號登入系統";
        String Text1 = "由於你目前尚未登入Crowdin帳號因此無法使用 協助翻譯 功能。";
        String Text2 = "如需使用此功能，請先登入Crowdin帳號，登入教學點擊下方按鈕即可。";

        this.drawString(fontRenderer, Screen, (this.width / 2 - fontRenderer.getStringWidth(Text1) / 2) + 55, height - 65, 0xFF5555);
        this.drawString(fontRenderer, Text1, this.width / 2 - fontRenderer.getStringWidth(Text1) / 2, height - 50, TextColor);
        this.drawString(fontRenderer, Text2, this.width / 2 - fontRenderer.getStringWidth(Text2) / 2, height - 40, TextColor);

        Token.setMaxStringLength(500);
        Token.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == Info) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.x230ggwx63l4"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (button == Login) {
            if (Token.getText().equals("")) {
                SendMsg.send("Crowdin登入權杖不能是空的");
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else {
                SendMsg.send("正在嘗試登入中...");
                Minecraft.getMinecraft().displayGuiScreen(null);
                try {
                    new TokenCheck().Check(Token.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (button == Close) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        Token.textboxKeyTyped(c, i);
    }

    @Override
    public void mouseClicked(int i, int j, int k) throws IOException {
        Token.mouseClicked(i, j, k);
        super.mouseClicked(i, j, k);
    }

    @Override
    public void updateScreen() {
        Token.updateCursorCounter();
        super.updateScreen();
    }
}