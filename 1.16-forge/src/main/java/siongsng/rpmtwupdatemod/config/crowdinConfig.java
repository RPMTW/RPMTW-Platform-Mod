package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class crowdinConfig {
    public static ForgeConfigSpec.BooleanValue rpmtw_crowdin;

    public static void init(ForgeConfigSpec.Builder client) {
        client.comment("Crowdin Config");


        rpmtw_crowdin = client
                .comment("crowdin = 是否啟用點擊指定快捷鍵(預設為V)後開啟翻譯網頁，並顯示相關資訊。")
                .define("rpmtw.crowdin", true);
    }
}
