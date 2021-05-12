package siongsng.rpmtwupdatemod.gui;
/*

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class CrowidnGuiWindow extends ContainerScreen<CrowdinGui.GuiContainerMod> {
    private static final ResourceLocation texture = new ResourceLocation("rpmtw_update_mod:textures/crowdin_gui.png");
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final PlayerEntity entity;
    TextFieldWidget ttanslation;
    String Text = OpenCrowdinKeyBinding.getText();
    PlayerEntity p = container.entity;
    Item item = p.getHeldItemMainhand().getItem(); //拿的物品
    String mod_id = item.getCreatorModId(p.getHeldItemMainhand().getStack()); //物品所屬的模組ID
    String item_key = item.getTranslationKey(); //物品的命名空間
    String item_DisplayName = item.getName().getString(); //物品的顯示名稱
    String stringID = OpenCrowdinKeyBinding.stringID;

    public CrowidnGuiWindow(CrowdinGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.xSize = 405;
        this.ySize = 227;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
        ttanslation.render(ms, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeScreen();
            return true;
        }
        if (ttanslation.isFocused())
            return ttanslation.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void tick() {
        super.tick();
        ttanslation.tick();
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
        this.font.drawString(ms, "RPMTW 物品翻譯介面", 155, 12, -65536);
        this.font.drawString(ms, "原文: " + Text, 143, 43, -16777216);
        this.font.drawString(ms, "語系鍵: " + item_key, 143, 27, -16777216);
        this.font.drawString(ms, "顯示名稱: " + item_DisplayName, 143, 72, -16777216);
        this.font.drawString(ms, "所屬模組 ID: " + mod_id, 143, 58, -16777216);
    }

    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        minecraft.keyboardListener.enableRepeatEvents(true);
        ttanslation = new TextFieldWidget(this.font, this.guiLeft + 150, this.guiTop + 97, 120, 20, new StringTextComponent("請輸入譯文")) {
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
        CrowdinGui.guistate.put("text:ttanslation", ttanslation);
        ttanslation.setMaxStringLength(32767);
        this.children.add(this.ttanslation);
        this.addButton(new Button(this.guiLeft + 181, this.guiTop + 147, 57, 20, new StringTextComponent("提交翻譯"), e -> {

            if (ttanslation.getText().equals("")) {
                SendMsg.send("§4譯文不能是空的呦!");
            } else {
                SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + ttanslation.getText() + "§b 。");
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(
                    "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + ttanslation.getText() + "\"}",
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
            Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
        }));
        this.addButton(new Button(this.guiLeft + 253, this.guiTop + 147, 57, 20, new StringTextComponent("Crowdin"), e -> {
            String url = "https://translate.rpmtw.ga/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + stringID;

            p.sendMessage(new StringTextComponent("§6開啟翻譯平台網頁中..."), p.getUniqueID()); //發送訊息
            Util.getOSType().openURI(url); //使用預設瀏覽器開啟網頁
        }));
        this.addButton(new Button(this.guiLeft + 109, this.guiTop + 147, 57, 20, new StringTextComponent("關閉介面"), e -> {
            Minecraft.getInstance().displayGuiScreen(null);
            Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
        }));
    }
}
*/