package siongsng.rpmtwupdatemod.gui.CosmicChat;

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
import siongsng.rpmtwupdatemod.CosmicChat.SendMessage;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.function.SendMsg;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CosmicChatSend extends LightweightGuiDescription {
    public CosmicChatSend() {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(300, 215);
        WLabel label = new WLabel(new LiteralText("宇宙通訊系統-發送訊息介面"), 0xFF5555);
        gui.add(label, (int) 5.8, 1, 2, 1);
        gui.add(new WLabel(new LiteralText("使用此功能代表您同意 《RPMTW 宇宙通訊系統終端使用者授權合約》"), 0xFFFFFF), 1, 2, 2, 1);
        gui.add(new WLabel(new LiteralText("- 由於此功能與Discord串聯，請遵守《Discord使用者服務條款》"), 0xFFFFFF), 1, 3, 2, 1);
        gui.add(new WLabel(new LiteralText("- 訊息內容請不得以任何形式騷擾別人，否則我們有權封禁該帳號"), 0xFFFFFF), 1, 4, 2, 1);
        gui.add(new WLabel(new LiteralText("- 我們將會蒐集您的IP、Minecraft UUID/ID，IP僅用於封禁帳號"), 0xFFFFFF), 1, 5, 2, 1);
        gui.add(new WLabel(new LiteralText("- 我們將有權隨時更改本條款"), 0xFFFFFF), 1, 6, 2, 1);

        WButton Cancel = new WButton(new LiteralText("取消"));
        WButton Send = new WButton(new LiteralText("傳送"));
        WButton Info = new WButton(new LiteralText("這是什麼？"));
        gui.add(Cancel, 1, 10, 4, 2);
        gui.add(Send, 6, 10, 4, 2);
        gui.add(Info, 11, 10, 5, 2);

        WTextField Message = new WTextField(new LiteralText("請輸入要發送的訊息"));
        Message.setMaxLength(150);
        gui.add(Message, 3, (int) 7.5, 10, 2);

        Cancel.setOnClick(() -> MinecraftClient.getInstance().openScreen(null));
        Send.setOnClick(() -> {
            if (Message.getText().equals("")) {
                SendMsg.send("訊息不能是空的。");
            } else {
                new SendMessage().Send(Message.getText());
            }
            MinecraftClient.getInstance().openScreen(null);
        });
        Info.setOnClick(() -> {
            Util.getOperatingSystem().open("https://www.rpmtw.ga/Wiki/RPMTW-Update-Mod-Related#h.krxvof43ocod");
        });
        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png")));
    }
}
