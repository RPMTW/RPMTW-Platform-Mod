package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;

public class TokenCheck {
    public void Check(String token) throws IOException {
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri("https://api.crowdin.com/api/v2/projects/442446")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .build();
        HttpResponse response = client.execute(request);
        PlayerEntity p = Minecraft.getInstance().player;
        assert p != null;
        if (response.getStatusLine().getStatusCode() == 200) {
            SendMsg.send("§a檢測成功，您的Token是有效的。");
        } else {
            SendMsg.send("§c檢測失敗，Token無效，請再嘗試新增或至RPMTW官方網站尋求幫助。");
            Util.getOSType().openURI("https://www.rpmtw.ga");
        }
    }
}
