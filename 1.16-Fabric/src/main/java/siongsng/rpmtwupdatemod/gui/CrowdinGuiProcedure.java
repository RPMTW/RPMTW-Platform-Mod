package siongsng.rpmtwupdatemod.gui;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.ConfigScreen;

import java.nio.charset.StandardCharsets;

public class CrowdinGuiProcedure {
    public static String responseBody;
    static ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();


    static String stringID = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("id").toString();

    public static Item item = MinecraftClient.getInstance().player.getMainHandStack().getItem();
    public static String mod_id = Registry.ITEM.getId(item).getNamespace();//物品所屬的模組ID
    public static String item_key = item.getTranslationKey(); //物品的命名空間
    public static String item_DisplayName = item.getName().getString(); //物品的顯示名稱

    public static String getText() {
        assert MinecraftClient.getInstance().player != null;
        String item_key = MinecraftClient.getInstance().player.getMainHandStack().getItem().getTranslationKey(); //拿的物品
        String Text;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + item_key)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.Token)
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
        }

        try {
            Text = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("text").toString();
        } catch (Exception e) {
            Text = "無法取得";
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
        }

        return Text;
    }
}
