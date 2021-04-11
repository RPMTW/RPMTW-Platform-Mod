package siongsng.rpmtwupdatemod.function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import siongsng.rpmtwupdatemod.config.Configer;

public class PlayerInfo {
    public PlayerEntity player;
    public BlockPos position;
    public boolean isAfk;
    public int afkTime;

    public PlayerInfo(PlayerEntity player) {
        this.player = player;
        this.position = player.getPosition();
        this.isAfk = false;
    }

    public boolean isAfk() {
        return afkTime >= Configer.afkTime.get() * 20;
    }

}