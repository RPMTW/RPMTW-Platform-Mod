package siongsng.rpmtwupdatemod;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class json {
    public static JSONObject get(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(json.toString());
    }

    public static Object loadJson(String url) {
        return get(url).getJSONArray("assets").getJSONObject(0).get("browser_download_url");
    }

    public static Object ver(String url) {
        return get(url).get("tag_name");
    }
}