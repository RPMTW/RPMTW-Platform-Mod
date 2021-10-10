package siongsng.rpmtwupdatemod.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configer {
    public static ForgeConfigSpec.BooleanValue rpmtw_crowdin, rpmtw_reloadpack, notice, afk, isCheck, isChinese, isChat, isEULA;
    public static ForgeConfigSpec.IntValue afkTime;
    public static ForgeConfigSpec.ConfigValue<List<String>> modBlackList;
    public static ForgeConfigSpec.ConfigValue<String> Token;

    public static void init(ForgeConfigSpec.Builder client) {

        rpmtw_crowdin = client
                .comment("crowdin = 是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
                .define("rpmtw.crowdin", true);
        client.comment("ReloadPack Config");

        rpmtw_reloadpack = client
                .comment("reloadpack = 是否啟用使用快捷鍵(預設為R)快速重新載入RPMTW繁體中文資源包。")
                .define("rpmtw.reloadpack", true);
        notice = client
                .comment("notice = 進入世界時，是否自動發送公告。(此變更須重啟遊戲後生效)")
                .define("rpmtw.notice", true);
        afk = client
                .comment("afk = 是否啟用掛機偵測，啟用後當掛機時會自動更新翻譯。")
                .define("rpmtw.afk", false);
        isCheck = client
                .define("rpmtw.isCheck", false);
        isChinese = client
                .comment("IsChinese = 預設進入遊戲時，是否自動切換語言為 繁體中文。")
                .define("rpmtw.isChinese", true);
        isChat = client
                .comment("isChat = 是否使用宇宙通訊系統 (包含接收訊息與發送訊息)。")
                .define("rpmtw.isChat", true);
        isEULA = client
                .comment("isEULA = 使用者是否同意宇宙通訊EULA。")
                .define("rpmtw.isEULA", false);
        afkTime = client
                .comment("afkTime = 此數值用來設定過多久沒有活動才會進入掛機模式。(以秒為單位)")
                .defineInRange("rpmtw.afkTime", 600, 10, 3600);
        Token = client
                .comment("Crowdin Token (翻譯平台登入權杖)")
                .define("rpmtw.token", "");
        modBlackList = client
                .comment("模組翻譯黑名單，可輸入資料夾 或 檔案名稱(若檔名未指定資料夾，將會排除所有同名檔案)。")
                .define("rpmtw.modBlackList", new ArrayList<String>());
    }
}