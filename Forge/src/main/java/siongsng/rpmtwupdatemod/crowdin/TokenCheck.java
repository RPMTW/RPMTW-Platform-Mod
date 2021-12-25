package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.utilities.SendMsg;

import java.io.IOException;

public class TokenCheck {

    public void Check(String token) throws IOException {
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
            if (response.getStatusLine().getStatusCode() == 200) {
                if (Minecraft.getMinecraft().player != null) {
                    SendMsg.send("§9[Crowdin權杖自動檢測系統]§a檢測成功，您的登入權杖是有效的。");
                }
                RpmtwUpdateMod.LOGGER.info("[Crowdin權杖自動檢測系統]§a檢測成功，您的登入權杖是有效的。");
                RPMTWConfig.Token = token;
                RPMTWConfig.isCheck = true;
            } else {
                if (Minecraft.getMinecraft().player != null) {
                    SendMsg.send("§9[Crowdin權杖自動檢測系統]§c檢測失敗，登入權杖無效，請再嘗試新增或至RPMTW官方Discord群組尋求協助。\n官方Discord群組:https://discord.gg/5xApZtgV2u");
                }
                RpmtwUpdateMod.LOGGER.info("[Crowdin權杖自動檢測系統]§c檢測失敗，登入權杖無效，請再嘗試新增或至RPMTW官方Discord群組尋求協助。\n官方Discord群組:https://discord.gg/5xApZtgV2u");
                RPMTWConfig.isCheck = false;
            }
            ConfigManager.sync(RpmtwUpdateMod.MOD_ID, Config.Type.INSTANCE);
        });
        thread.start();
    }
}