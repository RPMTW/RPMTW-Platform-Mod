package siongsng.rpmtwupdatemod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.io.IOException;

public class AddToken {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(customCommand());
    }

    private LiteralArgumentBuilder<CommandSource> customCommand() {
        return LiteralArgumentBuilder.<CommandSource>literal("crowdin-token")
                .then(Commands.argument("token", StringArgumentType.greedyString()).executes(this::execute)).executes(this::exe);
    }

    private int execute(CommandContext<CommandSource> ctx) {
        String cmd = ctx.getInput().split("\\s+")[1];
        try {
            Configer.Token.set(cmd);
            PlayerEntity p = Minecraft.getInstance().player;
            assert p != null;
            p.sendMessage(new StringTextComponent("§bCrowdin Token 新增完畢，正在準備開始檢查Token是否為有效的。"), p.getUniqueID());
            new TokenCheck().Check(cmd); //檢測Token
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int exe(CommandContext<CommandSource> ctx) {
        SendMsg.send("§c請輸入參數，§b/Crowdin-token <您的Token>");
        return 0;
    }
}
