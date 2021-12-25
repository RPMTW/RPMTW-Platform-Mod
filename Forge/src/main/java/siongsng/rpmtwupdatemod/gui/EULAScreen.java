/*
特別感謝 3X0DUS - ChAoS#6969 解惑關於此類別的一些問題。
 */
package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class EULAScreen extends GuiScreen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    GuiButton Info;
    GuiButton OK;
    GuiButton Close;
    protected String initMessage;

    public EULAScreen(String initMessage) {
        this.initMessage = initMessage;
    }

    @Override
    public void initGui() {
        super.initGui();
        Info = new GuiButton(
                1,
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "這是什麼?");

        OK = new GuiButton(
                2,
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "我同意");

        Close = new GuiButton(
                3,
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "我不同意");

        this.buttonList.add(Info);
        this.buttonList.add(OK);
        this.buttonList.add(Close);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "宇宙通訊系統-EULA";
        String Text1 = "使用宇宙通訊系統須遵守《RPMTW 宇宙通訊系統終端使用者授權合約》";
        String Text2 = "- 由於此功能與Discord串聯，請遵守《Discord使用者服務條款》";
        String Text3 = "- 訊息內容請不得以任何形式騷擾別人，也禁止使用任何攻擊手段攻擊宇宙通訊伺服器，否則我們有權封禁該帳號";
        String Text4 = "- 我們將會蒐集您的 Minecraft UUID/ID 與 登入憑證，憑證與UUID僅用於驗證與封禁帳號";
        String Text5 = "- 我們將有權隨時更改本條款";

        this.drawString(fontRenderer, Screen, (this.width / 2 - fontRenderer.getStringWidth(Screen) / 2), height - 65, 0xFF5555);
        this.drawString(fontRenderer, Text1, this.width / 2 - fontRenderer.getStringWidth(Text1) / 2, height - 50, TextColor);
        this.drawString(fontRenderer, Text2, this.width / 2 - fontRenderer.getStringWidth(Text2) / 2, height - 40, TextColor);
        this.drawString(fontRenderer, Text3, this.width / 2 - fontRenderer.getStringWidth(Text3) / 2, height - 30, TextColor);
        this.drawString(fontRenderer, Text4, this.width / 2 - fontRenderer.getStringWidth(Text4) / 2, height - 20, TextColor);
        this.drawString(fontRenderer, Text5, this.width / 2 - fontRenderer.getStringWidth(Text5) / 2, height - 10, TextColor);

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

        if (button == OK) {
            RPMTWConfig.isEULA = true;
            ConfigManager.sync(RpmtwUpdateMod.MOD_ID, Config.Type.INSTANCE);

            if (!initMessage.isEmpty()){
                SocketClient.sendMessage(initMessage);
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
            }

            Minecraft.getMinecraft().displayGuiScreen(new CosmicChatScreen());
        }

        if (button == Close) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}