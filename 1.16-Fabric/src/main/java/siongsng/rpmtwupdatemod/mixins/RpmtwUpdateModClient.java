package siongsng.rpmtwupdatemod.mixins;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.commands.AddToken;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.gui.ConfigScreen;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(MinecraftClient.class)
public class RpmtwUpdateModClient {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RunArgs args, CallbackInfo ci) {
        MinecraftClient.getInstance().options.language = "zh_tw";
        AutoConfig.register(ConfigScreen.class, Toml4jConfigSerializer::new); //註冊Config Gui
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("crowdin-token").then(argument("token", StringArgumentType.greedyString())).executes(AddToken::execute).executes(AddToken::execute));
        });//註冊指令
        key.onInitializeClient(); //註冊快捷鍵
    }
}
