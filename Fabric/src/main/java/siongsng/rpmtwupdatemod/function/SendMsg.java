package siongsng.rpmtwupdatemod.function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class SendMsg {
    public static void send(String msg) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity p = mc.player;
            assert p != null;
            p.sendMessage(Text.of(msg), false);
        } catch (Throwable ignored) {

        }
    }
}
