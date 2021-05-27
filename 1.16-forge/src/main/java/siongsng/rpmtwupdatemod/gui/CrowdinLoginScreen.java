package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nonnull;
import java.io.IOException;

public class CrowdinLoginScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    TextFieldWidget Token;
    int xSize = 300;
    int ySize = 150;

    public CrowdinLoginScreen() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {

        this.addButton(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("查看帳號登入教學"),
                button -> {
                    Util.getOSType().openURI("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.x230ggwx63l4"); //使用預設瀏覽器開啟網頁
                }));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("登入"),
                button -> {
                    if (Token.getText().equals("")) {
                        SendMsg.send("Crowdin登入權杖不能是空的");
                        Minecraft.getInstance().displayGuiScreen(null);
                    } else {
                        SendMsg.send("正在嘗試登入中...");
                        Minecraft.getInstance().displayGuiScreen(null);
                        try {
                            new TokenCheck().Check(Token.getText());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
        this.addButton(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("關閉"),
                button -> Minecraft.getInstance().displayGuiScreen(null)));

        Token = new TextFieldWidget(this.font, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20, new StringTextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入Crowdin登入權杖");
            }

            @Override
            public void writeText(String text) {
                super.writeText(text);
                if (getText().isEmpty())
                    setSuggestion("請輸入Crowdin登入權杖");
                else
                    setSuggestion(null);
            }

            @Override
            public void setCursorPosition(int pos) {
                super.setCursorPosition(pos);
                if (getText().isEmpty())
                    setSuggestion("請輸入Crowdin登入權杖");
                else
                    setSuggestion(null);
            }
        };
        Token.setMaxStringLength(200);
        this.children.add(Token);
        Token.setMaxStringLength(32767);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        blit(matrixStack, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        RenderSystem.disableBlend();

        this.renderBackground(matrixStack);

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "RPMTW X Crowdin 翻譯帳號登入系統";
        String Text1 = "由於你目前尚未登入Crowdin帳號因此無法使用 協助翻譯 功能。";
        String Text2 = "如需使用此功能，請先登入Crowdin帳號，登入教學點擊下方按鈕即可。";

        this.font.drawString(matrixStack, Screen, (this.width / (float) 2 - this.font.getStringWidth(Text1) / (float) 2) + 55, height - 65, 0xFF5555);
        this.font.drawString(matrixStack, Text1, this.width / (float) 2 - this.font.getStringWidth(Text1) / (float) 2, height - 50, TextColor);
        this.font.drawString(matrixStack, Text2, this.width / (float) 2 - this.font.getStringWidth(Text2) / (float) 2, height - 40, TextColor);

        Token.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose(); //關閉此Gui
    }
}
