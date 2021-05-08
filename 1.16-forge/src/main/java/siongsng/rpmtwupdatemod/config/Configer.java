package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configer {
    public static ForgeConfigSpec.BooleanValue rpmtw_crowdin, rpmtw_reloadpack, report_translation, notice, afk, discord, DiscordPrefix, isCheck;
    public static ForgeConfigSpec.IntValue Update_interval, afkTime;
    public static ForgeConfigSpec.ConfigValue<String> Token;

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
        afk = client
                .comment("afk = 是否啟用掛機偵測，啟用後當掛機時會自動更新翻譯。")
                .define("rpmtw.afk", true);
        discord = client
                .comment("discord = 是否啟用宇宙通訊系統。")
                .define("rpmtw.discord", true);
        DiscordPrefix = client
                .comment("DiscordPrefix = 是否啟用宇宙通訊系統發送訊息時需要前綴(預設為!)")
                .define("rpmtw.DiscordPrefix", true);
        isCheck = client
                .define("rpmtw.isCheck", true);
        Update_interval = client
                .comment("Update_interval = 每次啟動遊戲時，自動RPMTW更新時所檢查的版本間隔。(此變更須重啟遊戲後生效)")
                .defineInRange("rpmtw.Update_interval", 0, 0, 20);
        afkTime = client
                .comment("afkTime = 此數值用來設定過多久沒有活動才會進入掛機模式。(以秒為單位)")
                .defineInRange("rpmtw.afkTime", 600, 10, 3600);
        Token = client
                .comment("Crowdin Token (翻譯平台登入權杖)")
                .define("rpmtw.token", "");
    }
}