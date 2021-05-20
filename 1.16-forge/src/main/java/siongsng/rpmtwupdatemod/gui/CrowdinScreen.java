package siongsng.rpmtwupdatemod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nonnull;
import java.io.IOException;

public final class CrowdinScreen extends Screen {

    static final int BUTTON_HEIGHT = 20;
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private static final int TITLE_HEIGHT = 8;
    private static final int BOTTOM_BUTTON_WIDTH = 95;
    TextFieldWidget Translation;
    int xSize = 405;
    int ySize = 227;
    String Text = CorwidnProcedure.getText();
    String stringID = CorwidnProcedure.stringID;


    PlayerEntity p = Minecraft.getInstance().player;
    Item item = p.getHeldItemMainhand().getItem(); //拿的物品
    String mod_id = item.getCreatorModId(p.getHeldItemMainhand().getStack()); //物品所屬的模組ID
    String item_key = item.getTranslationKey(); //物品的命名空間
    String item_DisplayName = item.getName().getString(); //物品的顯示名稱

    public CrowdinScreen() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {

        this.addButton(new Button(
                (this.width / 2 + 50),
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("Crowdin"),
                button -> {
                    String url = "https://crowdin.com/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + stringID;

                    p.sendMessage(new StringTextComponent("§6開啟翻譯平台網頁中..."), p.getUniqueID()); //發送訊息
                    Util.getOSType().openURI(url); //使用預設瀏覽器開啟網頁
                }));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH + 50,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("提交翻譯"),
                button -> {
                    if (Translation.getText().equals("")) {
                        SendMsg.send("§4譯文不能是空的呦!");
                    } else {
                        SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + Translation.getText() + "§b 。(約十分鐘後將會將內容套用變更至翻譯包)");
                    }
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    StringEntity requestEntity = new StringEntity(
                            "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + Translation.getText() + "\"}",
                            ContentType.APPLICATION_JSON);
                    HttpPost postMethod = new HttpPost("https://api.crowdin.com/api/v2/projects/442446/translations");
                    postMethod.setHeader("Authorization", "Bearer " + Configer.Token.get());
                    postMethod.setEntity(requestEntity);
                    try {
                        httpClient.execute(postMethod);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    Minecraft.getInstance().displayGuiScreen(null);
                }));
        this.addButton(new Button(
                (this.width - 100) / 2 - BOTTOM_BUTTON_WIDTH,
                (this.height / 2) + 80,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("取消"),
                button -> {
                    Minecraft.getInstance().displayGuiScreen(null);
                }));

        Translation = new TextFieldWidget(this.font, (this.width / 2) - 50, (this.height / 2) + 10, 120, 20, new StringTextComponent("請輸入譯文")) {
            {
                setSuggestion("請輸入譯文");
            }

            @Override
            public void writeText(String text) {
                super.writeText(text);
                if (getText().isEmpty())
                    setSuggestion("請輸入譯文");
                else
                    setSuggestion(null);
            }

            @Override
            public void setCursorPosition(int pos) {
                super.setCursorPosition(pos);
                if (getText().isEmpty())
                    setSuggestion("請輸入譯文");
                else
                    setSuggestion(null);
            }
        };
        this.children.add(Translation);
        Translation.setMaxStringLength(32767);
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
            this.minecraft.player.closeScreen();
            return true;
        }
        if (Translation.isFocused())
            return Translation.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        blit(matrixStack, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        RenderSystem.disableBlend();

        this.renderBackground(matrixStack);

        int width = (this.width / 2);
        int height = (this.height / 2);
        int TextColor = 0xFFFFFF;
        String rpmTScreen = "物品翻譯介面";
        String originalText = "原文: " + Text;
        String langKey = "語系鍵: " + item_key;
        String displayName = "顯示名稱: " + item_DisplayName;
        String parentModID = "所屬模組 ID: " + mod_id;

        this.font.drawString(matrixStack, rpmTScreen, this.width / (float) 2 - this.font.getStringWidth(rpmTScreen) / (float) 2, height - 105, -65536);
        this.font.drawString(matrixStack, originalText, this.width / (float) 2 - this.font.getStringWidth(originalText) / (float) 2, height - 80, TextColor);
        this.font.drawString(matrixStack, langKey, this.width / (float) 2 - this.font.getStringWidth(langKey) / (float) 2, height - 65, TextColor);
        this.font.drawString(matrixStack, displayName, this.width / (float) 2 - this.font.getStringWidth(displayName) / (float) 2, height - 50, TextColor);
        this.font.drawString(matrixStack, parentModID, this.width / (float) 2 - this.font.getStringWidth(parentModID) / (float) 2, height - 35, TextColor);

        Translation.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose(); //關閉此Gui
    }
}
