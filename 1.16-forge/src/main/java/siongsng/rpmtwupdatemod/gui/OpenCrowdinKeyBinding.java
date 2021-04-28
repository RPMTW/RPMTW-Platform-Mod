package siongsng.rpmtwupdatemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.ModElements;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ModElements.ModElement.Tag
public class OpenCrowdinKeyBinding extends ModElements.ModElement {
    @OnlyIn(Dist.CLIENT)
    private KeyBinding keys;

    public OpenCrowdinKeyBinding(ModElements instance) {
        super(instance, 4);
        elements.addNetworkMessage(KeyBindingPressedMessage.class, KeyBindingPressedMessage::buffer, KeyBindingPressedMessage::new,
                KeyBindingPressedMessage::handler);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initElements() {
        keys = new KeyBinding("key.rpmtw_update_mod.open_crowdin", GLFW.GLFW_KEY_G, "key.categories.rpmtw");
        ClientRegistry.registerKeyBinding(keys);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().currentScreen == null) {
            if (event.getKey() == keys.getKey().getKeyCode()) {
                if (event.getAction() == GLFW.GLFW_PRESS) {
                    PlayerEntity p = Minecraft.getInstance().player;
                    Item item = p.getHeldItemMainhand().getItem(); //拿的物品

                    String item_key = item.getTranslationKey(); //物品的命名空間
                    if (item_key.equals("block.minecraft.air")) {
                        p.sendMessage(new StringTextComponent("§4請手持物品後再使用此功能。"), p.getUniqueID()); //發送訊息
                        return;
                    }

                    RpmtwUpdateMod.PACKET_HANDLER.sendToServer(new KeyBindingPressedMessage(0, 0));
                    pressAction(Minecraft.getInstance().player, 0, 0);
                }
            }
        }
    }

    public static class KeyBindingPressedMessage {
        int type, pressedms;

        public KeyBindingPressedMessage(int type, int pressedms) {
            this.type = type;
            this.pressedms = pressedms;
        }

        public KeyBindingPressedMessage(PacketBuffer buffer) {
            this.type = buffer.readInt();
            this.pressedms = buffer.readInt();
        }

        public static void buffer(KeyBindingPressedMessage message, PacketBuffer buffer) {
            buffer.writeInt(message.type);
            buffer.writeInt(message.pressedms);
        }

        public static void handler(KeyBindingPressedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                pressAction(context.getSender(), message.type, message.pressedms);
            });
            context.setPacketHandled(true);
        }
    }

    private static void pressAction(PlayerEntity entity, int type, int pressedms) {
        World world = entity.world;
        double x = entity.getPosX();
        double y = entity.getPosY();
        double z = entity.getPosZ();
        // security measure to prevent arbitrary chunk generation
        if (!world.isBlockLoaded(new BlockPos(x, y, z)))
            return;
        if (type == 0) {
            {
                Map<String, Object> $_dependencies = new HashMap<>();
                $_dependencies.put("entity", entity);
                $_dependencies.put("x", x);
                $_dependencies.put("y", y);
                $_dependencies.put("z", z);
                $_dependencies.put("world", world);
                OpenCorwidnProcedure.executeProcedure($_dependencies);
            }
        }
    }
}
