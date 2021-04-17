package siongsng.rpmtwupdatemod.config;

public class DiscordPrefix {
    public static String get(String message) {
        String msg = message;
        if (ConfigScreen.DiscordPrefix) {
            msg = message.split("^!")[1];
        }
        return msg;
    }

    public static String Prefix() {
        String Prefix = "";
        if (ConfigScreen.DiscordPrefix) {
            Prefix = "!";
        }
        return Prefix;
    }
}
