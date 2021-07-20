package siongsng.rpmtwupdatemod.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import siongsng.rpmtwupdatemod.function.CheckModVersion;
import siongsng.rpmtwupdatemod.function.SendMsg;

import javax.annotation.Nullable;

public class noticeCMD extends CommandBase {
    @Override
    public String getName() {
        return "Get-notice";
    }

    @Override
    @Nullable
    public String getUsage(ICommandSender sender) {
        return "顯示RPMTW公告";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        SendMsg.send(new CheckModVersion().notice());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
