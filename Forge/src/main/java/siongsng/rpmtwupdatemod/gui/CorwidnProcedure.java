package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;

import java.nio.charset.StandardCharsets;

public class CorwidnProcedure {
    public static String responseBody = "";
    public static String stringID;

    public static String getText() {
        String Text;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + Minecraft.getInstance().player.getHeldItemMainhand().getItem().getTranslationKey())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + Configer.Token.get())
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            Text = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("text").toString();
            stringID = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("id").toString();

            //14694在Crowdin的分支ID代表1.16版本
            if (!JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("branchId").equals(14694)) {
                Text = null;
                stringID = null;
            }
        } catch (Exception e) {
            Text = null;
            stringID = null;
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
        }

        return Text;
    }
}
