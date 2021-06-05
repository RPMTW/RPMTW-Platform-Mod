package siongsng.rpmtwupdatemod.function;


import me.shedaniel.autoconfig.AutoConfig;
import siongsng.rpmtwupdatemod.config.ConfigScreen;

public class AFK {
    public static long afkTime = 0;
    static ConfigScreen config = AutoConfig.getConfigHolder(ConfigScreen.class).getConfig();
    private static boolean wasAfk = false;
    private static long lastUpdate = 0;

    public static void tickAfkStatus() {
        if (System.nanoTime() - lastUpdate > 1e+9) {
            afkTime++;

            boolean afk = afkTime > config.AfkTime;

            if (afk && !wasAfk) {
                SendMsg.send("§6偵測到你開始掛機了! 因此開始自動檢測翻譯包版本並且做更新。");
                new ReloadPack();
            } else if (!afk && wasAfk) {
                SendMsg.send("§a你不再掛機了!");
            }
            wasAfk = afk;
            lastUpdate = System.nanoTime();
        }
    }
}
