package siongsng.rpmtwupdatemod.function;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import siongsng.rpmtwupdatemod.config.Configer;

public class PlayerInfo {
    public Player player; //玩家實體
    public BlockPos position;
    public boolean isAfk; //是否掛機
    public int afkTime; //掛機時間

    public PlayerInfo(Player player) {
        this.player = player;
        this.position = player.blockPosition();
        this.isAfk = false;
    }

    public boolean isAfk() {
        return afkTime >= Configer.afkTime.get() * 20; //20個Tick等於一秒
    }

}