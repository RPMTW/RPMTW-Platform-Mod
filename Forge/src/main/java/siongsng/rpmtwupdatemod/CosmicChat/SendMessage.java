package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class SendMessage {
    public void Send(String Message) {
        try {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            String IP = new GetIP().Get();
            assert player != null;
            String Data = String.format("{\"Type\":\"Client\",\"Message\":\"%s\",\"UserName\":\"%s\",\"UUID\":\"%s\",\"IP\":\"%s\"}", Message, player.getName().getString(), player.getUUID(), IP);
            player.displayClientMessage(new TextComponent("訊息發送中..."), true);
            new SocketClient().getSocket().connect().emit("message", new JsonParser().parse(Data));
        } catch (Exception err) {
            RpmtwUpdateMod.LOGGER.warn("發送宇宙通訊訊息時發生未知錯誤，原因: " + err);
        }
    }
}
