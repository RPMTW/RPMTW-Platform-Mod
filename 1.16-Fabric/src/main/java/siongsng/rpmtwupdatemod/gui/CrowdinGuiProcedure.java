package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.MinecraftClient;
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

public class CrowdinGuiProcedure {
    public static String responseBody;
    public static String stringID = "";

    public static String getText() {
        String Text = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + MinecraftClient.getInstance().player.getMainHandStack().getItem().getTranslationKey())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + Configer.config.Token)
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            Text = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("text").toString();
            stringID = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("id").toString();
        } catch (Exception e) {
            Text = "無法取得";
            stringID = "無法取得";
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
        }

        return Text;
    }
}
