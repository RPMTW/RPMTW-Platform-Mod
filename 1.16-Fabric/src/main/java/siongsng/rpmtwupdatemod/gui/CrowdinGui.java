package siongsng.rpmtwupdatemod.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.function.SendMsg;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CrowdinGui extends LightweightGuiDescription {
    public CrowdinGui() {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(405, 227);

        WLabel label = new WLabel(new LiteralText("RPMTW 物品翻譯介面"), 0xFF5555);
        gui.add(label, (int) 8.5, 0, 2, 1);

        gui.add(new WLabel(new LiteralText("原文: " + CrowdinGuiProcedure.getText()), 0x000000), (int) 8.5, 3, 2, 1);
        gui.add(new WLabel(new LiteralText("語系鍵: " + CrowdinGuiProcedure.item_key), 0x000000), (int) 8.5, 4, 2, 1);
        gui.add(new WLabel(new LiteralText("顯示名稱: " + CrowdinGuiProcedure.item_DisplayName), 0x000000), (int) 8.5, 5, 2, 1);
        gui.add(new WLabel(new LiteralText("所屬模組 ID: " + CrowdinGuiProcedure.mod_id), 0x000000), (int) 8.5, 6, 2, 1);


        WButton Close = new WButton(new LiteralText("關閉"));
        WButton Done = new WButton(new LiteralText("提交翻譯"));
        WButton Crowdin = new WButton(new LiteralText("Crowdin"));

        gui.add(Close, 3, 9, 4, 2);
        gui.add(Done, (int) 9.5, 9, 4, 2);
        gui.add(Crowdin, 15, 9, 4, 2);

        Close.setOnClick(() -> {
            MinecraftClient.getInstance().openScreen(null);
        });
        Done.setOnClick(() -> {
            //
        });
        Crowdin.setOnClick(() -> {
            String url = "https://translate.rpmtw.ga/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + CrowdinGuiProcedure.stringID;
            SendMsg.send("§6開啟翻譯平台網頁中...");
            Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
        });

        WTextField Tanslation = new WTextField(new LiteralText("請輸入譯文"));
        gui.add(Tanslation, (int) 9.5, 8, 6, 2);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png"), 8));
    }
}