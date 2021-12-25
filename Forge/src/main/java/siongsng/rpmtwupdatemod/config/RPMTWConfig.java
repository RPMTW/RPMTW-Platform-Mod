package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RPMTWConfig {
    public static ForgeConfigSpec.BooleanValue rpmtw_crowdin, rpmtw_reloadpack, notice, isCheck, isChinese, cosmicChat, isEULA, isTranslate, cosmicChatButton, contributorButton;
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
        isCheck = client
                .define("rpmtw.isCheck", false);
        isChinese = client
                .comment("IsChinese = 預設進入遊戲時，是否自動切換語言為 繁體中文。")
                .define("rpmtw.isChinese", true);
        cosmicChat = client
                .comment("isChat = 是否使用宇宙通訊系統 (包含接收訊息與發送訊息)。")
                .define("rpmtw.cosmicChat", true);
        isEULA = client
                .comment("isEULA = 使用者是否同意宇宙通訊EULA。")
                .define("rpmtw.isEULA", false);
        Token = client
                .comment("Crowdin Token (翻譯平台登入權杖)")
                .define("rpmtw.token", "");
        isTranslate = client
                .comment("isTranslate = 是否開啟機器翻譯功能。")
                .define("rpmtw.isTranslate", true);
        cosmicChatButton = client
                .comment("cosmicChatButton = 是否在原版聊天視窗中新增宇宙通訊發送按鈕。")
                .define("rpmtw.cosmicChatButton", true);
        contributorButton = client
                .comment("contributorButton = 是否在遊戲暫停視窗中顯示翻譯貢獻者按鈕。")
                .define("rpmtw.contributorButton", true);
    }
}