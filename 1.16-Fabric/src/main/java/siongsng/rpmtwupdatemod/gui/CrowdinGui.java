package siongsng.rpmtwupdatemod.gui;

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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class CrowdinGui extends LightweightGuiDescription {
    Item item = MinecraftClient.getInstance().player.getMainHandStack().getItem();
    String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
    String item_key = item.getTranslationKey(); //物品的命名空間
    String item_DisplayName = item.getName().getString(); //物品的顯示名稱
    String Text = CrowdinGuiProcedure.getText();
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

        Close.setOnClick(() -> {
            MinecraftClient.getInstance().openScreen(null);
        });

        WTextField Translations = new WTextField(new LiteralText("請輸入譯文"));
        gui.add(Translations, (int) 9.5, 7, 6, 2);

        Done.setOnClick(() -> {
            if (Translations.getText().equals("")) {
                SendMsg.send("§4譯文不能是空的呦!");
            } else {
                SendMsg.send("§b已成功提交翻譯，將 §e" + Text + " §b翻譯為 §e" + Translations.getText() + "§b 。");
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(
                    "{\"stringId\":\"" + stringID + "\",\"languageId\":\"zh-TW\",\"text\": \"" + Translations.getText() + "\"}",
                    ContentType.APPLICATION_JSON);
            HttpPost postMethod = new HttpPost("https://api.crowdin.com/api/v2/projects/442446/translations");
            postMethod.setHeader("Authorization", "Bearer " + Configer.config.Token);
            postMethod.setEntity(requestEntity);
            try {
                CloseableHttpResponse response = httpClient.execute(postMethod);
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.print(responseBody);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            MinecraftClient.getInstance().openScreen(null);
        });
        Crowdin.setOnClick(() -> {
            String url = "https://translate.rpmtw.ga/translate/resourcepack-mod-zhtw/all/en-zhtw?filter=basic&value=0#q=" + stringID;
            SendMsg.send("§6開啟翻譯平台網頁中...");
            Util.getOperatingSystem().open(url);   //使用預設瀏覽器開啟網頁
        });

        gui.validate(this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(createNinePatch(new Identifier(RpmtwUpdateMod.Mod_ID, "textures/crowdin_gui.png"), 8));
    }
}