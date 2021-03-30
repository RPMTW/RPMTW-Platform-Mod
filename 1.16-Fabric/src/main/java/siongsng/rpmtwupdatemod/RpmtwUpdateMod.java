package siongsng.rpmtwupdatemod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.commands.AddToken;
import siongsng.rpmtwupdatemod.crowdin.key;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");

    @Override
    public void onInitializeClient() {
        if (!ping.isConnect()) {
            LOGGER.error("您當前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。(此偵測僅對Windows作業系統有效)");
        }
        LOGGER.info("Hello RPMTW world!");
        key.onInitializeClient();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("crowdin-token").then(argument("token", StringArgumentType.greedyString())).executes(AddToken::execute));
        });
    }

}