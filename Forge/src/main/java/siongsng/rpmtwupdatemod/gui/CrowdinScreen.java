/*
特別感謝 3X0DUS - ChAoS#6969 解惑關於此類別的一些問題。
 */
package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class CrowdinScreen extends GuiScreen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    GuiTextField Translation;
    GuiButton Info;
    GuiButton Login;
    GuiButton Close;
    String Text = CrowdinProcedure.getText();
    String stringID = CrowdinProcedure.stringID;


    EntityPlayer p = Minecraft.getMinecraft().player;
    Item item = p.getHeldItemMainhand().getItem(); //拿的物品
    String mod_id = item.getCreatorModId(p.getHeldItemMainhand()); //物品所屬的模組ID
    String item_key = item.getTranslationKey(); //物品的命名空間
    String item_DisplayName = item.getItemStackDisplayName(p.getHeldItemMainhand()); //物品的顯示名稱

    @Override
    public void initGui() {
        super.initGui();
        Translation = new GuiTextField(1, fontRenderer, (this.width / 2) - 50, (this.height / 2) + 10, 120, 20);
        Info = new GuiButton(
                1,
                (this.width / 2 + 50),
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "Crowdin");

        Login = new GuiButton(
                2,
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "提交翻譯");

        Close = new GuiButton(
                3,
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "取消");

        this.buttonList.add(Info);
        this.buttonList.add(Login);
        this.buttonList.add(Close);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String rpmTScreen = "RPMTW 物品翻譯介面";
        String originalText = "原文: " + Text;
        String langKey = "語系鍵: " + item_key;
        String displayName = "顯示名稱: " + item_DisplayName;
        String parentModID = "所屬模組 ID: " + mod_id;

        this.drawString(fontRenderer, rpmTScreen, (this.width / 2 - fontRenderer.getStringWidth(rpmTScreen) / 2), height - 105, -65536);
        this.drawString(fontRenderer, originalText, this.width / 2 - fontRenderer.getStringWidth(originalText) / 2, height - 80, TextColor);
        this.drawString(fontRenderer, langKey, this.width / 2 - fontRenderer.getStringWidth(langKey) / 2, height - 65, TextColor);
        this.drawString(fontRenderer, displayName, this.width / 2 - fontRenderer.getStringWidth(displayName) / 2, height - 50, TextColor);
        this.drawString(fontRenderer, parentModID, this.width / 2 - fontRenderer.getStringWidth(parentModID) / 2, height - 35, TextColor);

        Translation.setMaxStringLength(500);
        Translation.drawTextBox();

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
            if (Translation.getText().equals("")) {
                SendMsg.send("§4譯文不能是空的呦!");
            } else {
                SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + Translation.getText() + "§b 。(約十分鐘後將會將內容套用變更至翻譯包)");
            }
            Minecraft.getMinecraft().displayGuiScreen(null);
            Thread thread = new Thread(() -> {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity(
                        "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + Translation.getText() + "\"}",
                        ContentType.APPLICATION_JSON);
                HttpPost postMethod = new HttpPost("https://api.crowdin.com/api/v2/projects/442446/translations");
                postMethod.setHeader("Authorization", "Bearer " + RPMTWConfig.Token);
                postMethod.setEntity(requestEntity);
                try {
                    httpClient.execute(postMethod);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            thread.start();
        }

        if (button == Close) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        Translation.textboxKeyTyped(c, i);
    }

    @Override
    public void mouseClicked(int i, int j, int k) throws IOException {
        Translation.mouseClicked(i, j, k);
        super.mouseClicked(i, j, k);
    }

    @Override
    public void updateScreen() {
        Translation.updateCursorCounter();
        super.updateScreen();
    }
}