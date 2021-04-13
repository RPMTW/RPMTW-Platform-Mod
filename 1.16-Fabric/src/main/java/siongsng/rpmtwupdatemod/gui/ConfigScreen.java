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
    @Comment("進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
    public boolean notice = true;
    @Comment("是否啟用掛機偵測，啟用後當掛機時會自動更新翻譯。")
    public boolean afk = true;
    @Comment("是否啟用宇宙通訊系統。")
    public boolean discord = true;
    @Comment("每次啟動遊戲時，自動RPMTW更新時所檢查的版本間隔。(此變更須重啟遊戲後生效)")
    public int Update_interval = 0;
    @Comment("此數值用來設定過多久沒有活動才會進入掛機模式。(以秒為單位)")
    public int afkTime = 600;
}
