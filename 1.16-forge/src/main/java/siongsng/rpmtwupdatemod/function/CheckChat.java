package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckChat {
    public boolean get() {
        String ver = "1.0.7";
        return json.ver("https://api.github.com/repos/SiongSng/RPMTW-Update-Mod/releases/latest").toString().equals(ver);
    }
}