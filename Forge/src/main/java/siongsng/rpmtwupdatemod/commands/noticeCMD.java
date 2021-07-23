package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class noticeCMD {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(customCommand()); //註冊指令
    }

    private LiteralArgumentBuilder<CommandSourceStack> customCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("Get-notice").executes(this::exe);
    }

    private int exe(CommandContext<CommandSourceStack> ctx) {
        try {
            SendMsg.send(new TranslatableComponent("gui.notice").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

