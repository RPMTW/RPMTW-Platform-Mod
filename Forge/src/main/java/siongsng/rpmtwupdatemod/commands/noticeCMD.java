package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class noticeCMD {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(customCommand()); //註冊指令
    }

    private LiteralArgumentBuilder<CommandSource> customCommand() {
        return LiteralArgumentBuilder.<CommandSource>literal("Get-notice").executes(this::exe);
    }

    private int exe(CommandContext<CommandSource> ctx) {
        try {
            SendMsg.send(new TranslationTextComponent("gui.notice").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

