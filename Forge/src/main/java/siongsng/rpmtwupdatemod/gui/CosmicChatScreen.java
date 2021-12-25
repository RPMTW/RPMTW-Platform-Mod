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
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.utilities.SendMsg;

import javax.annotation.Nonnull;

public class CosmicChatScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    TextFieldWidget Message;
    int xSize = 300;
    int ySize = 150;

    public CosmicChatScreen() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {

        this.addButton(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("這是什麼?"),
                button -> Util.getOSType().openURI("https://www.rpmtw.ga/Wiki/ModInfo#what-is-cosmic-system")));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("傳送"),
                button -> {
                    if (Message.getText().equals("")) {
                        SendMsg.send("訊息不能是空的。");
                    } else {
                        SocketClient.sendMessage(Message.getText());
                    }
                    Minecraft.getInstance().displayGuiScreen(null);
                }));
        this.addButton(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("取消"),
                button -> Minecraft.getInstance().displayGuiScreen(null)));

        Message = new TextFieldWidget(this.font, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20, new StringTextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入要發送的訊息");
            }

            @Override
            public void writeText(String text) {
                super.writeText(text);
                if (getText().isEmpty())
                    setSuggestion("請輸入要發送的訊息");
                else
                    setSuggestion(null);
            }

            @Override
            public void setCursorPosition(int pos) {
                super.setCursorPosition(pos);
                if (getText().isEmpty())
                    setSuggestion("請輸入要發送的訊息");
                else
                    setSuggestion(null);
            }
        };
        Message.setMaxStringLength(150);
        this.children.add(Message);
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
        String Screen = "宇宙通訊系統-發送訊息介面";

        this.font.drawString(matrixStack, Screen, (this.width / (float) 2 / (float) 2) + 55, height - 65, 0xFF5555);

        Message.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose(); //關閉此Gui
    }
}
