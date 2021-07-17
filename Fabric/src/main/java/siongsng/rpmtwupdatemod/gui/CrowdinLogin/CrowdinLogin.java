package siongsng.rpmtwupdatemod.gui.CrowdinLogin;

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
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CrowdinLogin extends LightweightGuiDescription {
    public CrowdinLogin() {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(250, 150);
        WLabel label = new WLabel(new LiteralText("RPMTW X Crowdin 翻譯帳號登入系統"), 0xFF5555);
        gui.add(label, (int) 4.8, 0, 2, 1);
        gui.add(new WLabel(new LiteralText("由於你目前尚未登入Crowdin帳號因此無法使用 協助翻譯 功能。"), 0xFFFFFF), 1, 1, 2, 1);
        gui.add(new WLabel(new LiteralText("如需使用此功能，請先登入Crowdin帳號，登入教學點擊下方按鈕即可。"), 0xFFFFFF), 1, 2, 2, 1);

        WButton Close = new WButton(new LiteralText("關閉"));
        WButton Done = new WButton(new LiteralText("登入"));
        WButton Info = new WButton(new LiteralText("查看帳號登入教學"));
        gui.add(Close, 1, 7, 4, 2);
        gui.add(Done, 6, 7, 4, 2);
        gui.add(Info, 11, 7, 5, 2);

        WTextField Token = new WTextField(new LiteralText("請輸入Crowdin登入權杖"));
        Token.setMaxLength(200);
        gui.add(Token, 3, (int) 4.5, 10, 2);

        Close.setOnClick(() -> MinecraftClient.getInstance().openScreen(null));
        Info.setOnClick(() -> Util.getOperatingSystem().open("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.x230ggwx63l4"));
        Done.setOnClick(() -> {
            if (Token.getText().equals("")) {
                SendMsg.send("Crowdin登入權杖不能是空的");
                MinecraftClient.getInstance().openScreen(null);
            } else {
                SendMsg.send("正在嘗試登入中...");
                MinecraftClient.getInstance().openScreen(null);
                new TokenCheck().Check(Token.getText());
            }
        });
        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png")));
    }
}
