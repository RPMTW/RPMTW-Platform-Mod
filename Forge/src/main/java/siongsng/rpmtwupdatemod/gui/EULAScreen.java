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
import siongsng.rpmtwupdatemod.config.Configer;

import javax.annotation.Nonnull;

public class EULAScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    int xSize = 300;
    int ySize = 150;

    public EULAScreen() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {

        this.addButton(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("我不同意"),
                button -> Minecraft.getInstance().displayGuiScreen(null)));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("這是什麼?"),
                button -> Util.getOSType().openURI("https://www.rpmtw.ga/Wiki/ModInfo#what-is-cosmic-system")));
        this.addButton(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("我同意"),
                button -> {
                    Configer.isEULA.set(true);
                    Minecraft.getInstance().displayGuiScreen(new CosmicChatScreen());
                }));
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
        String Screen = "宇宙通訊系統-EULA";
        String Text1 = "使用宇宙通訊系統須遵守《RPMTW 宇宙通訊系統終端使用者授權合約》";
        String Text2 = "- 由於此功能與Discord串聯，請遵守《Discord使用者服務條款》";
        String Text3 = "- 訊息內容請不得以任何形式騷擾別人，也禁止使用任何攻擊手段攻擊宇宙通訊伺服器，否則我們有權封禁該帳號";
        String Text4 = "- 我們將會蒐集您的 Minecraft UUID/ID 與 登入憑證，憑證與UUID僅用於驗證與封禁帳號";
        String Text5 = "- 我們將有權隨時更改本條款";

        this.font.drawString(matrixStack, Screen, (this.width / (float) 2 - this.font.getStringWidth(Text1) / (float) 2) + 55, height - 65, 0xFF5555);
        this.font.drawString(matrixStack, Text1, this.width / (float) 2 - this.font.getStringWidth(Text1) / (float) 2, height - 50, TextColor);
        this.font.drawString(matrixStack, Text2, this.width / (float) 2 - this.font.getStringWidth(Text2) / (float) 2, height - 40, TextColor);
        this.font.drawString(matrixStack, Text3, this.width / (float) 2 - this.font.getStringWidth(Text2) / (float) 2, height - 30, TextColor);
        this.font.drawString(matrixStack, Text4, this.width / (float) 2 - this.font.getStringWidth(Text2) / (float) 2, height - 20, TextColor);
        this.font.drawString(matrixStack, Text5, this.width / (float) 2 - this.font.getStringWidth(Text2) / (float) 2, height - 10, TextColor);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose(); //關閉此Gui
    }
}
