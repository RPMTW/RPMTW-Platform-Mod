package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configer {
    public static ForgeConfigSpec.BooleanValue rpmtw_crowdin, rpmtw_reloadpack, report_translation, notice;
    public static ForgeConfigSpec.IntValue Update_interval;

    public static void init(ForgeConfigSpec.Builder client) {
        rpmtw_crowdin = client
                .comment("crowdin = 是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
                .define("rpmtw.crowdin", true);
        client.comment("ReloadPack Config");

        rpmtw_reloadpack = client
                .comment("reloadpack = 是否啟用使用快捷鍵(預設為R)快速重新載入RPMTW繁體中文資源包。")
                .define("rpmtw.reloadpack", true);
        report_translation = client
                .comment("report_translation = 是否啟用使用快捷鍵(預設為U)回報翻譯錯誤。")
                .define("rpmtw.report_translation", true);
        notice = client
                .comment("notice = 進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
                .define("rpmtw.notice", true);
        Update_interval = client
                .comment("Update_interval = 每次啟動遊戲時，自動RPMTW更新時所檢查的版本間隔。(此變更須重啟遊戲後生效)")
                .defineInRange("rpmtw.Update_interval", 0, 0, 20);
    }
}