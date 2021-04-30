package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;

public class AddToken {
    public static String token;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Crowdin-token").then(CommandManager.argument("token", StringArgumentType.greedyString()).executes(context -> {
            String cmd = context.getInput().split("\\s+")[1];
            token = cmd;
            SendMsg.send("§bCrowdin Token 新增完畢，正在準備開始檢查Token是否為有效的。");
            try {
                new TokenCheck().Check(cmd); //檢測Token
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Command.SINGLE_SUCCESS;
        })).executes(context -> {
            SendMsg.send("§c請輸入參數，§b/Crowdin-token <您的Token>");
            return Command.SINGLE_SUCCESS;
        }));
    }
}