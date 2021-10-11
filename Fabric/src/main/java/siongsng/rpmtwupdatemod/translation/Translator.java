package siongsng.rpmtwupdatemod.translation;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Translator {
    public String translate(String text) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + "en_us" + "&tl=" + "zh_Hant" + "&dt=t&q=" + text)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() == 429) return "錯誤：連線Google伺服器流量異常";

        String result = response.body().string();
        System.out.println(result);

        result = result.split("\n")[0];
        result = result.replace("\"", "").replace("[", "").replace("]", "");
        String[] filter = result.split(",");

        result = filter[0];

        client.dispatcher().cancelAll();
        return result;
    }

}
