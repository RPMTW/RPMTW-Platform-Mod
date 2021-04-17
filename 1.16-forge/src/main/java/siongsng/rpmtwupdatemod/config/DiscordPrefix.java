package siongsng.rpmtwupdatemod.config;

public class DiscordPrefix {
    public static String get(String message) {
        String msg = message;
        if (Configer.DiscordPrefix.get()) {
            msg = message.split("^!")[1];
        }
        return msg;
    }

    public static String Prefix() {
        String Prefix = "";
        if (Configer.DiscordPrefix.get()) {
            Prefix = "!";
        }
        return Prefix;
    }
}