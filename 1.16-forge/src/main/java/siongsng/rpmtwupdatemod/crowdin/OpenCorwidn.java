package siongsng.rpmtwupdatemod.crowdin;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import siongsng.rpmtwupdatemod.ModElements;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.util.Map;

@ModElements.ModElement.Tag
public class OpenCorwidn extends ModElements.ModElement {
    public OpenCorwidn(ModElements instance) {
        super(instance, 4);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            if (!dependencies.containsKey("entity"))
                RpmtwUpdateMod.LOGGER.warn("Failed to load dependency entity for procedure OpenCorwidn!");
            return;
        }
        if (dependencies.get("x") == null) {
            if (!dependencies.containsKey("x"))
                RpmtwUpdateMod.LOGGER.warn("Failed to load dependency x for procedure OpenCorwidn!");
            return;
        }
        if (dependencies.get("y") == null) {
            if (!dependencies.containsKey("y"))
                RpmtwUpdateMod.LOGGER.warn("Failed to load dependency y for procedure OpenCorwidn!");
            return;
        }
        if (dependencies.get("z") == null) {
            if (!dependencies.containsKey("z"))
                RpmtwUpdateMod.LOGGER.warn("Failed to load dependency z for procedure OpenCorwidn!");
            return;
        }
        if (dependencies.get("world") == null) {
            if (!dependencies.containsKey("world"))
                RpmtwUpdateMod.LOGGER.warn("Failed to load dependency world for procedure OpenCorwidn!");
            return;
        }
        Entity entity = (Entity) dependencies.get("entity");
        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
        IWorld world = (IWorld) dependencies.get("world");
        {
            if (entity instanceof ServerPlayerEntity) {
                BlockPos _bpos = new BlockPos((int) x, (int) y, (int) z);
                NetworkHooks.openGui((ServerPlayerEntity) entity, new INamedContainerProvider() {
                    @Override
                    public @NotNull ITextComponent getDisplayName() {
                        return new StringTextComponent("CrowdinGui");
                    }

                    @Override
                    public Container createMenu(int id, @NotNull PlayerInventory inventory, @NotNull PlayerEntity player) {
                        return new CrowdinGui.GuiContainerMod(id, inventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(_bpos));
                    }
                }, _bpos);
            }
        }
    }
}
