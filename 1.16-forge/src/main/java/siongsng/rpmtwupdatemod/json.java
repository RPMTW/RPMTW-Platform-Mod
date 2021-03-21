package siongsng.rpmtwupdatemod;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

public class json {
    public static JSONObject get() {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest");
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
            System.out.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(json.toString());
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