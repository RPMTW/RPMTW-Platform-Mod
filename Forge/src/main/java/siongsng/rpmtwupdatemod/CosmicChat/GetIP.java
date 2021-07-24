package siongsng.rpmtwupdatemod.CosmicChat;

import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetIP {
    public String Get() {
        String IP = null;
        String IPUrl = ("https://checkip.amazonaws.com");
        try {
            var IPStream = new BufferedReader(new InputStreamReader(new URL(IPUrl).openStream()));
            IP = IPStream.readLine();
            IPStream.close();
        } catch (IOException e) {
            RpmtwUpdateMod.LOGGER.error("取得IP失敗。\n原因:" + e);
        }
        return IP;
    }
}