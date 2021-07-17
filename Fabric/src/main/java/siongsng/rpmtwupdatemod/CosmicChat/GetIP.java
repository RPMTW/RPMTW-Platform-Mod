package siongsng.rpmtwupdatemod.CosmicChat;

import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIP {
    public String Get(){
        String IP = "127.0.1";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            RpmtwUpdateMod.LOGGER.error("取得IP失敗。\n原因:" + e);
        }
        return IP;
    }
}
