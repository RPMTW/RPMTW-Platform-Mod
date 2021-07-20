package siongsng.rpmtwupdatemod.function;


public class CheckModVersion {
    public String ver = "1.2.1";

    public boolean get() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.12").getAsJsonPrimitive("latest").getAsString().equals(ver);
    }

    public String NewVer() {
        return json.get("https://raw.githubusercontent.com/RPMTW/ResourcePack-Mod-zh_tw/main/ModUpdate.json").getAsJsonObject("Forge").getAsJsonObject("1.12").getAsJsonPrimitive("latest").getAsString();
    }

}