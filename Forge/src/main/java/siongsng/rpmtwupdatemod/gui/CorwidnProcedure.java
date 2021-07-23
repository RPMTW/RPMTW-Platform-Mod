package siongsng.rpmtwupdatemod.gui;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import java.nio.charset.StandardCharsets;

public class CorwidnProcedure {
    public static String responseBody = "";
    public static String stringID;

    public static String getText() {
        String Text;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + Minecraft.getInstance().player.getMainHandItem().getItem().getDescriptionId())
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
}
