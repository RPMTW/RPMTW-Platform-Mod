package siongsng.rpmtwupdatemod.gui;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.nio.charset.StandardCharsets;

public class CrowdinProcedure {
    public static String responseBody = "";
    public static String stringID;
    public static ItemStack item;

    public static String getText(String key) {
        String Text;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + key)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + RPMTWConfig.Token.get())
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            JsonParser jp = new JsonParser();
            Text = jp.parse(responseBody).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject().get("data").getAsJsonObject().getAsJsonPrimitive("text").getAsString();
            stringID = jp.parse(responseBody).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject().get("data").getAsJsonObject().getAsJsonPrimitive("id").getAsString();
        } catch (Exception e) {
            Text = null;
            stringID = null;
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
        }

        return Text;
    }

    public static void OpenTransactionGUI(ItemStack itemStack) {
        item = itemStack;
        SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
        Thread thread = new Thread(() -> {
            if (CrowdinProcedure.getText(item.getDescriptionId()) == null && RPMTWConfig.isCheck.get()) {
                SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                return;
            }
            Minecraft.getInstance().setScreen(new CrowdinScreen());
        });
        thread.start();
    }
}
