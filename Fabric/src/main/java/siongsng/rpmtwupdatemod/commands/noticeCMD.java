package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class noticeCMD {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Get-notice").executes(context -> {
            context.getSource().getPlayer().sendMessage(new TranslatableText(
                    "gui.notice"), false);
            return Command.SINGLE_SUCCESS;
        }));
    }
}
