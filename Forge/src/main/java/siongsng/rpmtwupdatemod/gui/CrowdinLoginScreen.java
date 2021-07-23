package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nonnull;
import java.io.IOException;

public class CrowdinLoginScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    EditBox Token;

    public CrowdinLoginScreen() {
        super(new TextComponent(""));
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("查看帳號登入教學"),
                button -> {
                    Util.getPlatform().openUri("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.x230ggwx63l4"); //使用預設瀏覽器開啟網頁
                }));

        this.addRenderableWidget(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("登入"),
                button -> {
                    if (Token.getValue().equals("")) {
                        SendMsg.send("Crowdin登入權杖不能是空的");
                        Minecraft.getInstance().setScreen(null);
                    } else {
                        SendMsg.send("正在嘗試登入中...");
                        Minecraft.getInstance().setScreen(null);
                        try {
                            new TokenCheck().Check(Token.getValue());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
        this.addRenderableWidget(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("關閉"),
                button -> Minecraft.getInstance().setScreen(null)));

        Token = new EditBox(this.font, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20, new TextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入Crowdin登入權杖");
            }

            @Override
            public void insertText(String text) {
                super.insertText(text);
                if (getValue().isEmpty())
                    setSuggestion("請輸入Crowdin登入權杖");
                else
                    setSuggestion(null);
            }

            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                if (getValue().isEmpty())
                    setSuggestion("請輸入Crowdin登入權杖");
                else
                    setSuggestion(null);
            }
        };
        Token.setMaxLength(200);
        this.addWidget(Token);
        Token.setMaxLength(32767);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "RPMTW X Crowdin 翻譯帳號登入系統";
        String Text1 = "由於你目前尚未登入Crowdin帳號因此無法使用 協助翻譯 功能。";
        String Text2 = "如需使用此功能，請先登入Crowdin帳號，登入教學點擊下方按鈕即可。";

        this.font.draw(matrixStack, Screen, this.width / (float) 2 - this.font.width(Screen) / (float) 2, height - 65, 0xFF5555);
        this.font.draw(matrixStack, Text1, this.width / (float) 2 - this.font.width(Text1) / (float) 2, height - 50, TextColor);
        this.font.draw(matrixStack, Text2, this.width / (float) 2 - this.font.width(Text2) / (float) 2, height - 40, TextColor);

        Token.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        super.removed(); //關閉此Gui
    }
}
