package siongsng.rpmtwupdatemod.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.crowdin.key;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class ConfigScreen extends Screen {

    /**
     * 此類部分原始碼取至:
     * https://leo3418.github.io/zh/2020/09/09/forge-mod-config-screen.html
     */

    private static final ConfigManager CMI = ConfigManager.getInstance();
    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int BOTTOM_BUTTON_WIDTH = 150;
    static final int BUTTONS_INTERVAL = 4;
    static final int BUTTON_HEIGHT = 20;
    private static final int BOTTOM_BUTTON_HEIGHT_OFFSET = 26;
    private OptionsRowList optionsRowList;

    public ConfigScreen() {
        super(new StringTextComponent("RPMTW自動繁化模組 設定選單"));
    }

    @Override
    protected void init() {
        assert this.minecraft != null;

        optionsRowList = new OptionsRowList(
                Objects.requireNonNull(this.minecraft), this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT);

        optionsRowList.addOption(new BooleanOption(
                String.format("開啟對應翻譯網頁 (快捷鍵%s)", key.crowdin.getKeyModifier().toString()),
                unused -> Configer.rpmtw_crowdin.get(),
                (unused, newValue) -> Configer.rpmtw_crowdin.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                String.format("快速重新載入資源包 (快捷鍵%s)", key.reloadpack.getKey().toString().split("key.keyboard.")[1]),
                unused -> Configer.rpmtw_reloadpack.get(),
                (unused, newValue) -> Configer.rpmtw_reloadpack.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                String.format("回報翻譯錯誤 (快捷鍵%s)", key.report_translation.getKey().toString().split("key.keyboard.")[1]),
                unused -> Configer.report_translation.get(),
                (unused, newValue) -> Configer.report_translation.set(newValue)
        ));

        this.children.add(optionsRowList);
        this.addButton(new Button(
                this.width / 3,
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("更多資訊"),
                button -> Util.getOSType().openURI("https://www.rpmtw.ga"))
        );
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        // 儲存模組設定
        CMI.save();
        super.onClose();
    }
}