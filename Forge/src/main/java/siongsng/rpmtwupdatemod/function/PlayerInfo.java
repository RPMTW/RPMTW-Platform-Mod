package siongsng.rpmtwupdatemod.function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import siongsng.rpmtwupdatemod.config.Configer;

public class PlayerInfo {
    public PlayerEntity player; //玩家實體
    public BlockPos position;
    public boolean isAfk; //是否掛機
    public int afkTime; //掛機時間

    public PlayerInfo(PlayerEntity player) {
        this.player = player;
        this.position = player.getPosition();
        this.isAfk = false;
    }

    public boolean isAfk() {
        return afkTime >= Configer.afkTime.get() * 20; //20個Tick等於一秒
    }

}