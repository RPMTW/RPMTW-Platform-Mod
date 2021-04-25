package siongsng.rpmtwupdatemod.crowdin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

@OnlyIn(Dist.CLIENT)
public class CrowdinGuiWindow extends ContainerScreen<CrowdinGui.GuiContainerMod> {
    private World world;
    private int x, y, z;
    private PlayerEntity entity;
    TextFieldWidget ttanslation;

    public CrowdinGuiWindow(CrowdinGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.xSize = 180;
        this.ySize = 238;
    }

    private static final ResourceLocation texture = new ResourceLocation("test_forge:textures/crowdin_gui.png");

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
        ttanslation.render(ms, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float par1, int par2, int par3) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
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
        this.font.drawString(ms, "\u7FFB\u8B6F\u4ECB\u9762", 67, 4, -12829636);
        this.font.drawString(ms, "\u8ACB\u5C07\u8981\u7FFB\u8B6F\u7684\u7269\u54C1\u653E\u5982\u5176\u4E2D", 16, 25, -12829636);
        this.font.drawString(ms, "\u8B6F\u6587:", 13, 114, -12829636);
        this.font.drawString(ms, "\u539F\u6587: Copper Block", 52, 81, -12829636);
        this.font.drawString(ms, "\u7FFB\u8B6F\u9375: block.Copper_Ore", 37, 68, -12829636);
        this.font.drawString(ms, "\u76EE\u524D\u8B6F\u6587: \u7121", 65, 95, -12829636);
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
        ttanslation = new TextFieldWidget(this.font, this.guiLeft + 37, this.guiTop + 109, 120, 20, new StringTextComponent("請輸入譯文")) {
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
        this.addButton(new Button(this.guiLeft + 52, this.guiTop + 41, 73, 20, new StringTextComponent("讀取物品翻譯資訊"), e -> {
            RpmtwUpdateMod.PACKET_HANDLER.sendToServer(new CrowdinGui.ButtonPressedMessage(0, x, y, z));
            CrowdinGui.handleButtonAction(entity, 0, x, y, z);
        }));
        this.addButton(new Button(this.guiLeft + 61, this.guiTop + 133, 57, 20, new StringTextComponent("提交翻譯"), e -> {
            RpmtwUpdateMod.PACKET_HANDLER.sendToServer(new CrowdinGui.ButtonPressedMessage(1, x, y, z));
            CrowdinGui.handleButtonAction(entity, 1, x, y, z);
        }));
    }

}
