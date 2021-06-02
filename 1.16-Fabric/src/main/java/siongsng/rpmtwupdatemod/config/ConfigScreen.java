package siongsng.rpmtwupdatemod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "rpmtw_update_mod")
public class ConfigScreen implements ConfigData {
    @Comment("是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
    public boolean crowdin = true;
    @Comment("是否啟用使用快捷鍵(預設為R)檢測RPMTW翻譯包更新。")
    public boolean ReloadPack = true;
    @Comment("是否啟用使用快捷鍵(預設為U)回報翻譯錯誤。")
    public boolean ReportTranslation = true;
    @Comment("進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
    public boolean notice = true;
    @Comment("是否啟用掛機偵測，啟用後當掛機時會自動更新翻譯。")
    public boolean afk = false;
    @Comment("每次啟動遊戲時，自動RPMTW翻譯包更新時所檢查的版本間隔。(此變更須重啟遊戲後生效)")
    public int UpdateInterval = 0;
    @Comment("數值以秒為單位")
    public int AfkTime = 600;
    @Comment("預設進入遊戲時，是否自動切換語言為 繁體中文。")
    public boolean isChinese = true;
    @ConfigEntry.Gui.Excluded
    public String Token = ""; //儲存Crowdin登入權杖
    @ConfigEntry.Gui.Excluded
    public boolean isCheck = false; //儲存登入權杖是否有效
}
