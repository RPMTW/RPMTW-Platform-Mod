package siongsng.rpmtwupdatemod.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class ConfigScreen extends Screen {

    static final int BUTTON_HEIGHT = 20;
    /**
     * 此類別部分原始碼取至:
     * https://leo3418.github.io/zh/2020/09/09/forge-mod-config-screen.html
     */

    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int BOTTOM_BUTTON_HEIGHT_OFFSET = 26;
    private static final int BOTTOM_BUTTON_WIDTH = 150;
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
                "開啟對應翻譯網頁",
                unused -> Configer.rpmtw_crowdin.get(),
                (unused, newValue) -> Configer.rpmtw_crowdin.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "使用快捷鍵檢測翻譯包更新",
                unused -> Configer.rpmtw_reloadpack.get(),
                (unused, newValue) -> Configer.rpmtw_reloadpack.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "是否啟用宇宙通訊系統",
                unused -> Configer.isChat.get(),
                (unused, newValue) -> Configer.isChat.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "進入世界時自動發送公告",
                unused -> Configer.notice.get(),
                (unused, newValue) -> Configer.notice.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "啟用機器翻譯",
                unused -> Configer.isTranslate.get(),
                (unused, newValue) -> Configer.isTranslate.set(newValue)
        ));

        this.children.add(optionsRowList);

        this.addButton(new Button(
                (this.width / 2),
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("重置設定"),
                button -> {
                    Configer.rpmtw_crowdin.set(true);
                    Configer.rpmtw_reloadpack.set(true);
                    Configer.notice.set(true);
                    Configer.isChat.set(true);
                    Configer.isTranslate.set(true);
                    Configer.isChinese.set(true);
                    Minecraft.getInstance().displayGuiScreen(new ConfigScreen());
                }));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH,
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("儲存設定"),
                button -> {
                    Config.save(); // 儲存模組設定
                    Minecraft.getInstance().displayGuiScreen(null);
                })
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
        Config.save(); // 儲存模組設定
        super.onClose(); //關閉此Gui
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint( //註冊組態螢幕至Forge模組設定
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );
    }
}