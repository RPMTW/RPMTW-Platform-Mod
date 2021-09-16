package siongsng.rpmtwupdatemod.crowdin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;

public class TokenCheck {
    ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();

    public void Check(String token) {
        Thread thread = new Thread(() -> {
            HttpClient client = HttpClients.custom().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446")
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader("Authorization", "Bearer " + token)
                    .build();
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert response != null;

            try(MinecraftClient mc = MinecraftClient.getInstance()){
            	if (response.getStatusLine().getStatusCode() == 200) {
                    if (mc.player != null) {
                        SendMsg.send("§9[Crowdin權杖自動檢測系統]§a檢測成功，您的登入權杖是有效的。");
                    }
                    RpmtwUpdateMod.LOGGER.info("[Crowdin權杖自動檢測系統]§a檢測成功，您的登入權杖是有效的。");
                    config.Token = token;
                    config.isCheck = true;
                    AutoConfig.getConfigHolder(ConfigScreen.class).save();
                } else {
                    if (mc.player != null) {
                        SendMsg.send("§9[Crowdin權杖自動檢測系統]§c檢測失敗，登入權杖無效，請再嘗試新增或至RPMTW官方Discord群組尋求協助。\n官方Discord群組:https://discord.gg/5xApZtgV2u");
                    }
                    RpmtwUpdateMod.LOGGER.info("[Crowdin權杖自動檢測系統]§c檢測失敗，登入權杖無效，請再嘗試新增或至RPMTW官方Discord群組尋求協助。\n官方Discord群組:https://discord.gg/5xApZtgV2u");
                    config.isCheck = false;
                }
            }
            
            
        });
        thread.start();
    }
}
