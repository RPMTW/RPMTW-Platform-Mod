package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckChat {
    public boolean get() {
        String ver = "1.0.8"; //目前版本
        return json.ver("https://api.github.com/repos/SiongSng/RPMTW-Update-Mod/releases/latest").toString().equals(ver); //取得最新版本編號
    }
}