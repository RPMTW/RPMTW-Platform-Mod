package siongsng.rpmtwupdatemod.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

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
    private OptionsList optionsRowList;

    public ConfigScreen() {
        super(new TextComponent("RPMTW自動繁化模組 設定選單"));
    }

    @Override
    protected void init() {
        assert this.minecraft != null;

        optionsRowList = new OptionsList(
                Objects.requireNonNull(this.minecraft), this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT);
        optionsRowList.addBig(CycleOption.createOnOff("是否啟用開啟物品翻譯界面",
                (values) -> RPMTWConfig.rpmtw_crowdin.get(),
                (p_168189_, p_168190_, newValue) -> RPMTWConfig.rpmtw_crowdin.set(newValue)
        ));
        optionsRowList.addBig(CycleOption.createOnOff("是否啟用使用快捷鍵更新翻譯包",
                (values) -> RPMTWConfig.rpmtw_reloadpack.get(),
                (p_168189_, p_168190_, newValue) -> RPMTWConfig.rpmtw_reloadpack.set(newValue)
        ));
        optionsRowList.addBig(CycleOption.createOnOff("是否啟用宇宙通訊系統",
                (values) -> RPMTWConfig.isChat.get(),
                (p_168189_, p_168190_, newValue) -> RPMTWConfig.isChat.set(newValue)
        ));
        optionsRowList.addBig(CycleOption.createOnOff("是否啟用進入世界時發送公告",
                (values) -> RPMTWConfig.notice.get(),
                (p_168189_, p_168190_, newValue) -> RPMTWConfig.notice.set(newValue)
        ));
        this.addWidget(optionsRowList);

        this.addRenderableWidget(new Button(
                (this.width / 2),
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("重置設定"),
                button -> {
                    RPMTWConfig.rpmtw_crowdin.set(true);
                    RPMTWConfig.rpmtw_reloadpack.set(true);
                    RPMTWConfig.notice.set(true);
                    RPMTWConfig.isChat.set(true);
                    RPMTWConfig.isChinese.set(true);
                    Minecraft.getInstance().setScreen(new ConfigScreen());
                }));

        this.addRenderableWidget(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH,
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("儲存設定"),
                button -> {
                    Config.save(); // 儲存模組設定
                    Minecraft.getInstance().setScreen(null);
                })
        );
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        Config.save(); // 儲存模組設定
        super.removed(); //關閉此Gui
    }
}