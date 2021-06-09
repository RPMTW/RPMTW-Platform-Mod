package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckModVersion {
    public String ver = "1.1.5";

    public boolean get() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.16").getAsJsonPrimitive("latest").getAsString().equals(ver);
    }

    public String NewVer() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.16").getAsJsonPrimitive("latest").getAsString();
    }

    public String DownloadUrl() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.16").getAsJsonPrimitive("DownloadUrl").getAsString();
    }

}