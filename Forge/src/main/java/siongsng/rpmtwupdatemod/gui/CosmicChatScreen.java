package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import siongsng.rpmtwupdatemod.CosmicChat.SendMessage;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nonnull;

public class CosmicChatScreen extends Screen {
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    EditBox Message;

    public CosmicChatScreen() {
        super(new TextComponent(""));
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("這是什麼?"),
                button -> Util.getPlatform().openUri("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.krxvof43ocod")));

        this.addRenderableWidget(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("傳送"),
                button -> {
                    if (Message.getValue().equals("")) {
                        SendMsg.send("訊息不能是空的。");
                    } else {
                        new SendMessage().Send(Message.getValue());
                    }
                    Minecraft.getInstance().setScreen(null);
                }));
        this.addRenderableWidget(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 30,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("取消"),
                button -> Minecraft.getInstance().setScreen(null)));

        Message = new EditBox(this.font, (this.width / 2) - 95, (this.height / 2) - 10, 200, 20, new TextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入要發送的訊息");
            }

            @Override
            public void insertText(String text) {
                super.insertText(text);
                if (getValue().isEmpty())
                    setSuggestion("請輸入要發送的訊息");
                else
                    setSuggestion(null);
            }

            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                if (getValue().isEmpty())
                    setSuggestion("請輸入要發送的訊息");
                else
                    setSuggestion(null);
            }
        };
        Message.setMaxLength(150);
        this.addWidget(Message);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int height = (this.height / 2);
        String Screen = "宇宙通訊系統-發送訊息介面";

        this.font.draw(matrixStack, Screen, this.width / (float) 2 / (float) 2 - 55, height - 65, 0xFF5555);

        Message.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        super.removed(); //關閉此Gui
    }
}
