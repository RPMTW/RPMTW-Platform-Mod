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
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.ModElements;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ModElements.ModElement.Tag
public class OpenCrowdinKeyBinding extends ModElements.ModElement {
    @OnlyIn(Dist.CLIENT)
    private KeyBinding keys;
    public static String responseBody = "";

    public OpenCrowdinKeyBinding(ModElements instance) {
        super(instance, 4);
        elements.addNetworkMessage(KeyBindingPressedMessage.class, KeyBindingPressedMessage::buffer, KeyBindingPressedMessage::new,
                KeyBindingPressedMessage::handler);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initElements() {
        keys = new KeyBinding("key.rpmtw_update_mod.open_crowdin", GLFW.GLFW_KEY_V, "key.categories.rpmtw");
        ClientRegistry.registerKeyBinding(keys);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static String getText() {
        String item_key = Minecraft.getInstance().player.getHeldItemMainhand().getItem().getTranslationKey(); //拿的物品
        String Text = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.crowdin.com/api/v2/projects/442446/strings?filter=" + item_key)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + Configer.Token.get())
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
        }

        try {
            Text = JSONObject.fromObject(responseBody).getJSONArray("data").getJSONObject(0).getJSONObject("data").get("text").toString();
        } catch (Exception e) {
            Text = "無法取得";
            RpmtwUpdateMod.LOGGER.error("讀取翻譯資訊時發生錯誤: " + e.getMessage());
        }

        return Text;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().currentScreen == null) {
            if (event.getKey() == keys.getKey().getKeyCode()) {
                if (event.getAction() == GLFW.GLFW_PRESS) {
                    try {
                        PlayerEntity p = Minecraft.getInstance().player;
                        Item item = p.getHeldItemMainhand().getItem(); //拿的物品


                        String item_key = item.getTranslationKey(); //物品的命名空間
                        if (item_key.equals("block.minecraft.air")) {
                            p.sendMessage(new StringTextComponent("§4請手持物品後再使用此功能。"), p.getUniqueID()); //發送訊息
                            return;
                        } else if (!new TokenCheck().isCheck && Configer.Token.get().equals("")) {
                            SendMsg.send("§c請先新增Crowdin(翻譯平台)的登入權杖新增，再使用該功能或至RPMTW官方Discord群組尋求協助。\n§aRPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                            return;
                        } else if (getText().equals("無法取得") && !Configer.Token.get().equals("")) {
                            SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                            return;
                        }
                        RpmtwUpdateMod.PACKET_HANDLER.sendToServer(new KeyBindingPressedMessage(0, 0));
                        pressAction(p, 0, 0);
                    } catch (Exception e) {
                        RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
                    }
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
