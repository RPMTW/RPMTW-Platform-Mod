package siongsng.rpmtwupdatemod.gui;

import com.google.gson.JsonObject;
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
import siongsng.rpmtwupdatemod.utilities.SendMsg;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


public record CrowdinInfo(String sourceText, String stringID, ItemStack itemStack) {

    public String getSourceText() {
        return sourceText;
    }

    public String getStringID() {
        return stringID;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static CrowdinInfo getByItem(ItemStack itemStack) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + itemStack.getDescriptionId())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + RPMTWConfig.Token.get())
                    .build();

            CloseableHttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject().get("data").getAsJsonObject();
            String sourceText = jsonObject.getAsJsonPrimitive("text").getAsString();
            String stringID = jsonObject.getAsJsonPrimitive("id").getAsString();
            return new CrowdinInfo(sourceText, stringID, itemStack);
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
            return null;
        }
    }


    public static void openTransactionGUI(ItemStack itemStack) {
        SendMsg.send("請稍後，正在讀取物品翻譯資訊中...");
        Thread crowdinAPIThread = new Thread(() -> {
            AtomicReference<CrowdinInfo> info = new AtomicReference<>();
            info.set(getByItem(itemStack));
            if (info.get() == null && RPMTWConfig.isCheck.get()) {
                SendMsg.send("§6由於你指定想要翻譯的物品或實體生怪蛋，不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
            } else if (info.get() != null) {
                RpmtwUpdateMod.LOGGER.info("test");
                Minecraft.getInstance().setScreen(new CrowdinScreen(info.get()));
            }
        });
        crowdinAPIThread.start();

    }
}