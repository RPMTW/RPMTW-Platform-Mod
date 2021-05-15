package siongsng.rpmtwupdatemod.function;

import siongsng.rpmtwupdatemod.json;

public class CheckModVersion {
    public String ver = "1.1.2";

    public boolean get() {
        return json.get("https://raw.githubusercontent.com/SiongSng/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Fabric").getJSONObject("1.16").get("latest").toString().equals(ver);
    }

    public String NewVer() {
        return json.get("https://raw.githubusercontent.com/SiongSng/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Fabric").getJSONObject("1.16").get("latest").toString();
    }

    public String DownloadUrl() {
        return json.get("https://raw.githubusercontent.com/SiongSng/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getJSONObject("Fabric").getJSONObject("1.16").get("DownloadUrl").toString();
    }

}