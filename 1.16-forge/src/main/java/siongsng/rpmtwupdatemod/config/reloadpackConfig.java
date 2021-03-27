package siongsng.rpmtwupdatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class reloadpackConfig {
    public static ForgeConfigSpec.BooleanValue rpmtw_reloadpack;

    public static void init(ForgeConfigSpec.Builder client) {
        client.comment("ReloadPack Config");


        rpmtw_reloadpack = client
                .comment("reloadpack = 是否啟用使用快捷鍵(預設為R)快速重新載入RPMTW繁體中文資源包。")
                .define("rpmtw.reloadpack", true);
    }
}
