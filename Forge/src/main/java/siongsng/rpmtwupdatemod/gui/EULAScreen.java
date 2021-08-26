package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import javax.annotation.Nonnull;

public class EULAScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;

    public EULAScreen() {
        super(new TextComponent(""));
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("我不同意"),
                button -> Minecraft.getInstance().setScreen(null)));

        this.addRenderableWidget(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("這是什麼?"),
                button -> Util.getPlatform().openUri("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.krxvof43ocod")));
        this.addRenderableWidget(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("我同意"),
                button -> {
                    RPMTWConfig.isEULA.set(true);
                    Minecraft.getInstance().setScreen(new CosmicChatScreen());
                }));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF; //白色
        String Screen = "宇宙通訊系統-EULA";
        String Text1 = "使用宇宙通訊系統須遵守《RPMTW 宇宙通訊系統終端使用者授權合約》";
        String Text2 = "- 由於此功能與Discord串聯，請遵守《Discord使用者服務條款》";
        String Text3 = "- 訊息內容請不得以任何形式騷擾別人，也禁止使用任何攻擊手段攻擊宇宙通訊伺服器，否則我們有權封禁該帳號";
        String Text4 = "- 我們將會蒐集您的 Minecraft UUID/ID 與 登入憑證，憑證與UUID僅用於驗證與封禁帳號";
        String Text5 = "- 我們將有權隨時更改本條款";

        this.font.draw(matrixStack, Screen, this.width / (float) 2 - this.font.width(Screen) / (float) 2, height - 65, 0xFF5555);
        this.font.draw(matrixStack, Text1, this.width / (float) 2 - this.font.width(Text1) / (float) 2, height - 50, TextColor);
        this.font.draw(matrixStack, Text2, this.width / (float) 2 - this.font.width(Text2) / (float) 2, height - 40, TextColor);
        this.font.draw(matrixStack, Text3, this.width / (float) 2 - this.font.width(Text2) / (float) 2, height - 30, TextColor);
        this.font.draw(matrixStack, Text4, this.width / (float) 2 - this.font.width(Text2) / (float) 2, height - 20, TextColor);
        this.font.draw(matrixStack, Text5, this.width / (float) 2 - this.font.width(Text2) / (float) 2, height - 10, TextColor);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        super.removed(); //關閉此Gui
    }
}
