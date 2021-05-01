package siongsng.rpmtwupdatemod.gui;


import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import siongsng.rpmtwupdatemod.ModElements;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ModElements.ModElement.Tag
public class CrowdinGui extends ModElements.ModElement {
    public static HashMap guistate = new HashMap();
    private static ContainerType<GuiContainerMod> containerType = null;

    public CrowdinGui(ModElements instance) {
        super(instance, 3);
        elements.addNetworkMessage(ButtonPressedMessage.class, ButtonPressedMessage::buffer, ButtonPressedMessage::new,
                ButtonPressedMessage::handler);
        elements.addNetworkMessage(GUISlotChangedMessage.class, GUISlotChangedMessage::buffer, GUISlotChangedMessage::new,
                GUISlotChangedMessage::handler);
        containerType = new ContainerType<>(new GuiContainerModFactory());
        FMLJavaModLoadingContext.get().getModEventBus().register(new ContainerRegisterHandler());
    }

    static void handleButtonAction(PlayerEntity entity, int buttonID, int x, int y, int z) {
        World world = entity.world;
        // security measure to prevent arbitrary chunk generation
        if (!world.isBlockLoaded(new BlockPos(x, y, z)))
            return;
    }

    private static void handleSlotAction(PlayerEntity entity, int slotID, int changeType, int meta, int x, int y, int z) {
        World world = entity.world;
        // security measure to prevent arbitrary chunk generation
        if (!world.isBlockLoaded(new BlockPos(x, y, z)))
            return;
    }

    @OnlyIn(Dist.CLIENT)
    public void initElements() {
        DeferredWorkQueue.runLater(() -> ScreenManager.registerFactory(containerType, CrowidnGuiWindow::new));
    }

    private static class ContainerRegisterHandler {
        @SubscribeEvent
        public void registerContainer(RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(containerType.setRegistryName("crowdin_gui"));
        }
    }

    public static class GuiContainerModFactory implements IContainerFactory {
        public GuiContainerMod create(int id, PlayerInventory inv, PacketBuffer extraData) {
            return new GuiContainerMod(id, inv, extraData);
        }
    }

    public static class GuiContainerMod extends Container implements Supplier<Map<Integer, Slot>> {
        World world;
        PlayerEntity entity;
        int x, y, z;
        private final IItemHandler internal;
        private final Map<Integer, Slot> customSlots = new HashMap<>();
        private final boolean bound = false;

        public GuiContainerMod(int id, PlayerInventory inv, PacketBuffer extraData) {
            super(containerType, id);
            this.entity = inv.player;
            this.world = inv.player.world;
            this.internal = new ItemStackHandler(0);
            BlockPos pos = null;
            if (extraData != null) {
                pos = extraData.readBlockPos();
                this.x = pos.getX();
                this.y = pos.getY();
                this.z = pos.getZ();
            }
        }

        public Map<Integer, Slot> get() {
            return customSlots;
        }

        @Override
        public boolean canInteractWith(PlayerEntity player) {
            return true;
        }
    }

    public static class ButtonPressedMessage {
        int buttonID, x, y, z;

        public ButtonPressedMessage(PacketBuffer buffer) {
            this.buttonID = buffer.readInt();
            this.x = buffer.readInt();
            this.y = buffer.readInt();
            this.z = buffer.readInt();
        }

        public ButtonPressedMessage(int buttonID, int x, int y, int z) {
            this.buttonID = buttonID;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static void buffer(ButtonPressedMessage message, PacketBuffer buffer) {
            buffer.writeInt(message.buttonID);
            buffer.writeInt(message.x);
            buffer.writeInt(message.y);
            buffer.writeInt(message.z);
        }

        public static void handler(ButtonPressedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                PlayerEntity entity = context.getSender();
                int buttonID = message.buttonID;
                int x = message.x;
                int y = message.y;
                int z = message.z;
                handleButtonAction(entity, buttonID, x, y, z);
            });
            context.setPacketHandled(true);
        }
    }

    public static class GUISlotChangedMessage {
        int slotID, x, y, z, changeType, meta;

        public GUISlotChangedMessage(int slotID, int x, int y, int z, int changeType, int meta) {
            this.slotID = slotID;
            this.x = x;
            this.y = y;
            this.z = z;
            this.changeType = changeType;
            this.meta = meta;
        }

        public GUISlotChangedMessage(PacketBuffer buffer) {
            this.slotID = buffer.readInt();
            this.x = buffer.readInt();
            this.y = buffer.readInt();
            this.z = buffer.readInt();
            this.changeType = buffer.readInt();
            this.meta = buffer.readInt();
        }

        public static void buffer(GUISlotChangedMessage message, PacketBuffer buffer) {
            buffer.writeInt(message.slotID);
            buffer.writeInt(message.x);
            buffer.writeInt(message.y);
            buffer.writeInt(message.z);
            buffer.writeInt(message.changeType);
            buffer.writeInt(message.meta);
        }

        public static void handler(GUISlotChangedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                PlayerEntity entity = context.getSender();
                int slotID = message.slotID;
                int changeType = message.changeType;
                int meta = message.meta;
                int x = message.x;
                int y = message.y;
                int z = message.z;
                handleSlotAction(entity, slotID, changeType, meta, x, y, z);
            });
            context.setPacketHandled(true);
        }
    }
}


