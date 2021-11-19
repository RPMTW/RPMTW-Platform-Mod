package siongsng.rpmtwupdatemod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = "rpmtw_update_mod")
public class ConfigScreen implements ConfigData {
    @Comment("是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
    public boolean crowdin = true;
    @Comment("是否啟用使用快捷鍵(預設為R)檢測RPMTW翻譯包更新。")
    public boolean ReloadPack = true;
    @Comment("進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
    public boolean notice = true;
    @Comment("預設進入遊戲時，是否自動切換語言為 繁體中文。")
    public boolean isChinese = true;
    @Comment("是否使用宇宙通訊系統。")
    public boolean isChat = true;
    public boolean isTranslate = true;
    @ConfigEntry.Gui.Excluded
    public String Token = ""; //儲存Crowdin登入權杖
    @ConfigEntry.Gui.Excluded
    public boolean isCheck = false; //儲存登入權杖是否有效
    @ConfigEntry.Gui.Excluded
    public boolean isEULA = false; //使用者是否同意宇宙通訊EULA
    @Comment("模組翻譯黑名單，可輸入資料夾 或 檔案名稱(若檔名未指定資料夾，將會排除所有同名檔案)。")
    public List<String> modBlackList = new ArrayList<String>(); //模組翻譯黑名單，可輸入資料夾名稱，或是檔案名稱
}