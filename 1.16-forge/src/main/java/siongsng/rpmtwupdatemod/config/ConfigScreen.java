package siongsng.rpmtwupdatemod.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;

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
                "重新載入翻譯包",
                unused -> Configer.rpmtw_reloadpack.get(),
                (unused, newValue) -> Configer.rpmtw_reloadpack.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "回報翻譯錯誤",
                unused -> Configer.report_translation.get(),
                (unused, newValue) -> Configer.report_translation.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "進入世界時自動發送公告",
                unused -> Configer.notice.get(),
                (unused, newValue) -> Configer.notice.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "啟用掛機偵測",
                unused -> Configer.afk.get(),
                (unused, newValue) -> Configer.afk.set(newValue)
        ));
        optionsRowList.addOption(new SliderPercentageOption(
                "開始偵測掛機間隔時間(秒)",
                10.0F, 3600, 1.0F,
                unused -> (double) Configer.afkTime.get(),
                (unused, newValue) -> Configer.afkTime.set(newValue.intValue()),
                (gs, option) -> new StringTextComponent("開始偵測掛機間隔時間(秒)" + ": " + (int) option.get(gs))));
        optionsRowList.addOption(new SliderPercentageOption(
                "自動檢查更新版本間隔",
                0.0F, 20, 1.0F,
                unused -> (double) Configer.Update_interval.get(),
                (unused, newValue) -> Configer.Update_interval.set(newValue.intValue()),
                (gs, option) -> new StringTextComponent("自動檢查更新版本間隔" + ": " + (int) option.get(gs))));
        optionsRowList.addOption(new BooleanOption(
                "啟用宇宙通訊系統",
                unused -> Configer.discord.get(),
                (unused, newValue) -> Configer.discord.set(newValue)
        ));
        optionsRowList.addOption(new BooleanOption(
                "啟用宇宙通訊發送訊息時需要前綴(!)",
                unused -> Configer.DiscordPrefix.get(),
                (unused, newValue) -> Configer.DiscordPrefix.set(newValue)
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
                    Configer.report_translation.set(true);
                    Configer.notice.set(true);
                    Configer.afk.set(true);
                    Configer.discord.set(true);
                    Configer.DiscordPrefix.set(true);
                    Configer.Update_interval.set(0);
                    Configer.afkTime.set(600);
                }));

        this.addButton(new Button(
                (this.width - 4) / 2 - BOTTOM_BUTTON_WIDTH,
                this.height - BOTTOM_BUTTON_HEIGHT_OFFSET,
                BOTTOM_BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("完成"),
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
}