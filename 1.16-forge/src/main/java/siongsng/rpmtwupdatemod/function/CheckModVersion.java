package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckModVersion {
    public String ver = "1.1.3";

    public boolean get() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Forge").getJSONObject("1.16").get("latest").toString().equals(ver);
    }

    public String NewVer() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Forge").getJSONObject("1.16").get("latest").toString();
    }

    public String DownloadUrl() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Forge").getJSONObject("1.16").get("DownloadUrl").toString();
    }

}