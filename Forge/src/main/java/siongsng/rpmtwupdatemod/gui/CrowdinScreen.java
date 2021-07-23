package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nonnull;
import java.io.IOException;

public final class CrowdinScreen extends Screen {

    static final int BUTTON_HEIGHT = 20;
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    EditBox Translation;
    String Text = CorwidnProcedure.getText();
    String stringID = CorwidnProcedure.stringID;


    Player p = Minecraft.getInstance().player;
    Item item = p.getMainHandItem().getItem(); //拿的物品
    String mod_id = item.getCreatorModId(p.getMainHandItem()); //物品所屬的模組ID
    String item_key = item.getDescriptionId(); //物品的命名空間
    String item_DisplayName = item.getDescription().getString(); //物品的顯示名稱

    public CrowdinScreen() {
        super(new TextComponent(""));
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("Crowdin"),
                button -> {
                    String url = "https://crowdin.com/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + stringID;

                    p.sendMessage(new TextComponent("§6開啟翻譯平台網頁中..."), p.getUUID()); //發送訊息
                    Util.getPlatform().openUri(url); //使用預設瀏覽器開啟網頁
                }));

        this.addRenderableWidget(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("提交翻譯"),
                button -> {
                    if (Translation.getValue().equals("")) {
                        SendMsg.send("§4譯文不能是空的呦!");
                        Minecraft.getInstance().setScreen(null);
                        return;
                    } else {
                        SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + Translation.getValue() + "§b 。(約十分鐘後將會將內容套用變更至翻譯包)");
                        Minecraft.getInstance().setScreen(null);
                    }
                    Thread thread = new Thread(() -> {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        StringEntity requestEntity = new StringEntity(
                                "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + Translation.getValue() + "\"}",
                                ContentType.APPLICATION_JSON);
                        HttpPost postMethod = new HttpPost("https://api.crowdin.com/api/v2/projects/442446/translations");
                        postMethod.setHeader("Authorization", "Bearer " + RPMTWConfig.Token.get());
                        postMethod.setEntity(requestEntity);
                        try {
                            httpClient.execute(postMethod);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    thread.start();
                }));
        this.addRenderableWidget(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("取消"),
                button -> {
                    Minecraft.getInstance().setScreen(null);
                }));

        Translation = new EditBox(this.font, (this.width / 2) - 50, (this.height / 2) + 10, 120, 20, new TextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入譯文");
            }

            @Override
            public void insertText(String text) {
                super.insertText(text);
                if (getValue().isEmpty())
                    setSuggestion("請輸入譯文");
                else
                    setSuggestion(null);
            }

            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                if (getValue().isEmpty())
                    setSuggestion("請輸入譯文");
                else
                    setSuggestion(null);
            }
        };
        this.addWidget(Translation);
        Translation.setMaxLength(32767);
    }

    @Override
    public void tick() {
        super.tick();
        Translation.tick();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            this.minecraft.player.closeContainer();
            return true;
        }
        if (Translation.isFocused())
            return Translation.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF;
        String rpmTScreen = "物品翻譯介面";
        String originalText = "原文: " + Text;
        String langKey = "語系鍵: " + item_key;
        String displayName = "顯示名稱: " + item_DisplayName;
        String parentModID = "所屬模組 ID: " + mod_id;

        this.font.draw(matrixStack, rpmTScreen, this.width / (float) 2 - this.font.width(rpmTScreen) / (float) 2, height - 105, -65536);
        this.font.draw(matrixStack, originalText, this.width / (float) 2 - this.font.width(originalText) / (float) 2, height - 80, TextColor);
        this.font.draw(matrixStack, langKey, this.width / (float) 2 - this.font.width(langKey) / (float) 2, height - 65, TextColor);
        this.font.draw(matrixStack, displayName, this.width / (float) 2 - this.font.width(displayName) / (float) 2, height - 50, TextColor);
        this.font.draw(matrixStack, parentModID, this.width / (float) 2 - this.font.width(parentModID) / (float) 2, height - 35, TextColor);

        Translation.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        super.removed(); //關閉此Gui
    }
}
