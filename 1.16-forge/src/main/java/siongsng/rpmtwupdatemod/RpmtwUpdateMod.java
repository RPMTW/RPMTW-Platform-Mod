package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.commands.AddToken;
import siongsng.rpmtwupdatemod.commands.noticeCMD;
import siongsng.rpmtwupdatemod.config.Config;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.discord.Chat;
import siongsng.rpmtwupdatemod.discord.OnChat;
import siongsng.rpmtwupdatemod.function.AFK;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;
import siongsng.rpmtwupdatemod.notice.notice;

import java.io.IOException;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger(); //註冊紀錄器
    public final static String Mod_ID = "rpmtw_update_mod"; //模組ID

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation("rpmtw_update_mod", "rpmtw_update_mod"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public ModElements elements;

    public RpmtwUpdateMod() throws IOException {
        elements = new ModElements();
        MinecraftForge.EVENT_BUS.register(new ModFMLBusEvents(this));


        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init); //註冊監聽事件
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml"); //註冊組態
        Config.loadConfig(Config.CLIENT); //載入客戶端組態

        LOGGER.info("Hello RPMTW world!");
        if (!ping.isConnect()) { //判斷是否有網路
            LOGGER.error("你目前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。");
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
        new PackVersionCheck(); //資源包版本檢查
        new Chat(); //JDA帳號登入
    }

    @SubscribeEvent
    public void init(final FMLClientSetupEvent e) {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(new key());  //快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new AddToken()); //AddToken指令註冊
        MinecraftForge.EVENT_BUS.register(new noticeCMD()); //noticeCMD指令註冊
        if (Configer.notice.get()) { //判斷Config
            MinecraftForge.EVENT_BUS.register(new notice()); //玩家加入事件註冊
        }
        MinecraftForge.EVENT_BUS.register(new AFK()); //掛機事件註冊
        MinecraftForge.EVENT_BUS.register(new OnChat()); //客戶端聊天事件

        ModLoadingContext.get().registerExtensionPoint( //註冊組態螢幕至Forge模組設定
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );
    }

    private static class ModFMLBusEvents {
        private final RpmtwUpdateMod parent;

        ModFMLBusEvents(RpmtwUpdateMod parent) {
            this.parent = parent;
        }

        @SubscribeEvent
        public void serverLoad(FMLServerStartingEvent event) {
            this.parent.elements.getElements().forEach(element -> element.serverLoad(event));
        }
    }
}