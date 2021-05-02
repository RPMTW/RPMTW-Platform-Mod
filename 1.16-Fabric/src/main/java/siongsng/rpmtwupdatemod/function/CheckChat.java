package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckChat {
    public String ver = "1.0.9";

    public boolean get() {
        return json.ver("https://api.github.com/repos/SiongSng/RPMTW-Update-Mod/releases/latest").toString().equals(ver);
    }

    public String NewVer() {
        return json.ver("https://api.github.com/repos/SiongSng/RPMTW-Update-Mod/releases/latest").toString();
    }
}