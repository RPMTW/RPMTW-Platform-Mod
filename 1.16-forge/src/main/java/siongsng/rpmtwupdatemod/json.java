package siongsng.rpmtwupdatemod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

public class json {
    public static JSONObject get() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest")
                .request(MediaType.APPLICATION_JSON)
                .get();
        return JSONObject.fromObject(response.readEntity(String.class));
    }

    public static Object loadJson() {
        JSONObject aaa = get();
        return aaa.getJSONArray("assets").getJSONObject(0).get("browser_download_url");
    }

    public static Object ver() {
        JSONObject aaa = get();
        return aaa.get("tag_name");
    }
}