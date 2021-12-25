package siongsng.rpmtwupdatemod.gui.CrowdinGui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.utilities.SendMsg;

import java.io.IOException;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CrowdinGui extends LightweightGuiDescription {
    Item item = CrowdinGuiProcedure.item.getItem();
    String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
    String item_key = item.getTranslationKey(); //物品的命名空間
    String Text = CrowdinGuiProcedure.getText(item_key);
    String item_DisplayName = item.getName().getString(); //物品的顯示名稱
    String stringID = CrowdinGuiProcedure.stringID;

    public CrowdinGui() {
        WGridPanel gui = new WGridPanel();
        setRootPanel(gui);
        gui.setSize(405, 227);
        WLabel label = new WLabel(new LiteralText("RPMTW 物品翻譯介面"), 0xFF5555);
        gui.add(label, 9, 0, 2, 1);

        gui.add(new WLabel(new LiteralText("原文: " + Text), 0xFFFFFF), (int) 8.5, 2, 2, 1);
        gui.add(new WLabel(new LiteralText("語系鍵: " + item_key), 0xFFFFFF), (int) 8.5, 3, 2, 1);
        gui.add(new WLabel(new LiteralText("顯示名稱: " + item_DisplayName), 0xFFFFFF), (int) 8.5, 4, 2, 1);
        gui.add(new WLabel(new LiteralText("所屬模組 ID: " + mod_id), 0xFFFFFF), (int) 8.5, 5, 2, 1);

        WButton Close = new WButton(new LiteralText("關閉"));
        WButton Done = new WButton(new LiteralText("提交翻譯"));
        WButton Crowdin = new WButton(new LiteralText("Crowdin"));

        gui.add(Close, 3, 9, 4, 2);
        gui.add(Done, (int) 9.5, 9, 4, 2);
        gui.add(Crowdin, 15, 9, 4, 2);

        Close.setOnClick(() -> MinecraftClient.getInstance().setScreen(null));

        WTextField Translations = new WTextField(new LiteralText("請輸入譯文"));
        gui.add(Translations, (int) 9.5, 7, 6, 2);

        Done.setOnClick(() -> {
            if (Translations.getText().equals("")) {
                SendMsg.send("§4譯文不能是空的呦!");
                return;
            } else {
                SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + Translations.getText() + "§b 。(約十分鐘後將會將內容套用變更至翻譯包)");
                MinecraftClient.getInstance().setScreen(null);
            }
            Thread thread = new Thread(() -> {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity(
                        "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + Translations.getText() + "\"}",
                        ContentType.APPLICATION_JSON);
                HttpPost postMethod = new HttpPost("https://api.crowdin.com/api/v2/projects/442446/translations");
                postMethod.setHeader("Authorization", "Bearer " + RPMTWConfig.getConfig().Token);
                postMethod.setEntity(requestEntity);
                try {
                    httpClient.execute(postMethod);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            thread.start();
        });
        Crowdin.setOnClick(() -> {
            String url = "https://crowdin.com/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + stringID;
            SendMsg.send("§6開啟翻譯平台網頁中...");
            Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
        });
        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png")));
    }
}