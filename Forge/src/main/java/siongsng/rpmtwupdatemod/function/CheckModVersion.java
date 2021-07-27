package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckModVersion {
    public String ver = "1.2.2";

    public boolean get() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.17").getAsJsonPrimitive("latest").getAsString().equals(ver);
    }

    public String NewVer() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.17").getAsJsonPrimitive("latest").getAsString();
    }

    public String ChangeLog() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.17").getAsJsonPrimitive("ChangeLog").getAsString();
    }

}