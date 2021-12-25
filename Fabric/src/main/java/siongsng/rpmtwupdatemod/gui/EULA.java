package siongsng.rpmtwupdatemod.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class EULA extends LightweightGuiDescription {
    public EULA(String initMessage) {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(300, 200);
        WLabel label = new WLabel(new LiteralText("《RPMTW 宇宙通訊系統終端使用者授權合約》"), 0xFF5555);
        gui.add(label, (int) 6.8, 1, 2, 1);
        gui.add(new WLabel(new LiteralText("如需使用宇宙通訊系統須遵守以下條款，違者我們有權封禁該帳號"), 0xFFFFFF), 1, 3, 2, 1);

        gui.add(new WLabel(new LiteralText("- 此功能與Discord串聯，請遵守《Discord使用者服務條款》"), 0xFFFFFF), 1, 4, 2, 1);
        gui.add(new WLabel(new LiteralText("- 訊息不得以任何形式騷擾別人，也禁止攻擊宇宙通訊伺服器"), 0xFFFFFF), 1, 5, 2, 1);
        gui.add(new WLabel(new LiteralText("- 我們將會蒐集您的 Minecraft 憑證，僅用於驗證與封禁帳號"), 0xFFFFFF), 1, 6, 2, 1);
        gui.add(new WLabel(new LiteralText("- 我們將有權隨時更改本條款"), 0xFFFFFF), 1, 7, 2, 1);

        WButton Cancel = new WButton(new LiteralText("我不同意"));
        WButton Info = new WButton(new LiteralText("這是什麼?"));
        WButton OK = new WButton(new LiteralText("我同意"));
        gui.add(Cancel, 1, 9, 4, 2);
        gui.add(Info, 6, 9, 4, 2);
        gui.add(OK, 11, 9, 5, 2);

        Cancel.setOnClick(() -> MinecraftClient.getInstance().setScreen(null));
        Info.setOnClick(() -> Util.getOperatingSystem().open("https://www.rpmtw.com/Wiki/ModInfo#what-is-cosmic-system"));
        OK.setOnClick(() -> {
            RPMTWConfig.getConfig().isEULA = true;
            AutoConfig.getConfigHolder(ConfigScreen.class).save(); //儲存Config
            CosmicChat.open(initMessage);
        });
        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png")));
    }
}
