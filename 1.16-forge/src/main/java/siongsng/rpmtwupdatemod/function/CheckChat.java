package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckChat {
    public String ver = "1.0.8";
    public String NewVer;

    public boolean get() {
        NewVer = json.ver("https://api.github.com/repos/SiongSng/RPMTW-Update-Mod/releases/latest").toString();
        return NewVer.equals(ver);
    }
}