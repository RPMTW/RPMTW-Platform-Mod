package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import siongsng.rpmtwupdatemod.notice.notice;

public class noticeCMD {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Get-notice").executes(context -> {
            notice.send(context.getSource().getPlayer());
            return Command.SINGLE_SUCCESS;
        }));
    }
}
