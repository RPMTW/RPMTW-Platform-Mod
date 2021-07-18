package siongsng.rpmtwupdatemod.CosmicChat;

import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetIP {
    public String Get() {
        String IP = null;
        try {
            URL MyIP = new URL("https://checkip.amazonaws.com");
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(
                    MyIP.openStream()));
            IP = in.readLine();
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error("取得IP失敗。\n原因:" + e);
        }
        return IP;
    }
}