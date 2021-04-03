package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.notice.noticeGet;

public class noticeCMD {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(customCommand()); //註冊指令
    }

    private LiteralArgumentBuilder<CommandSource> customCommand() {
        return LiteralArgumentBuilder.<CommandSource>literal("Get-notice").executes(this::exe);
    }

    private int exe(CommandContext<CommandSource> ctx) {
        SendMsg.send(noticeGet.get().replace("\\n", "\n"));
        return 0;
    }
}

