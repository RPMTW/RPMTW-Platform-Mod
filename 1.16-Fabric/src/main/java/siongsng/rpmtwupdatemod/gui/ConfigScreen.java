package siongsng.rpmtwupdatemod.gui;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "rpmtw_update_mod")
public class ConfigScreen implements ConfigData {
    @Comment("是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
    public boolean crowdin = true;
    @Comment("是否啟用使用快捷鍵(預設為R)快速重新載入RPMTW繁體中文資源包。")
    public boolean reloadpack = true;
    @Comment("是否啟用使用快捷鍵(預設為U)回報翻譯錯誤。")
    public boolean report_translation = true;
}
