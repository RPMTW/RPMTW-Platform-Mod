package siongsng.rpmtwupdatemod.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Chat {
    //JDA-Java Discord Api
    public static JDA bot = null;

    public Chat() {
        JDABuilder jdaBuilder = JDABuilder.createDefault("AAAODMwNzQ3ODI3NTY3MTk4MjQ4.YHLMNA.k8yLYQeanyVnZ0oqV2le8otnMtE".split("^AAA")[1]); //看到這個是不是覺得很香呢? 別想了一點都不香。
        jdaBuilder.setActivity(Activity.playing("https://www.rpmtw.ga"));
        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        bot.addEventListener(new OnDiscordChat());
    }
}
