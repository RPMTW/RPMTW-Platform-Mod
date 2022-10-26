package com.rpmtw.rpmtw_platform_mod.mixins;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformModPlugin;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Screen.class)
@Environment(EnvType.CLIENT)
public class RunClientCommandOnComponent {
    @Redirect(method = "handleComponentClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;commandUnsigned(Ljava/lang/String;)Z"))
    public boolean handleComponentClick(LocalPlayer player, String command) {
        // Because Forge doesn't support click event on text component to run the client command.
        // So we have to implement it by ourselves.
        if (Platform.isForge() && command.startsWith("rpmtw")) {
            RPMTWPlatformModPlugin.executeClientCommand(command);
            return true;
        }

        return player.commandUnsigned(command);
    }
}
