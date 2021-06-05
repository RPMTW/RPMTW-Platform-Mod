package siongsng.rpmtwupdatemod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class json {
    public static JsonObject get(String url) {
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
        JsonParser jp = new JsonParser();
        return (JsonObject) jp.parse(json.toString());
    }

    public static Object ver(String url) {
        JsonParser jp = new JsonParser();
        JsonObject object = (JsonObject) jp.parse(get(url).toString());
        return object.get("tag_name").getAsString();
    }
}