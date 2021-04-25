package siongsng.rpmtwupdatemod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
import siongsng.rpmtwupdatemod.crowdin.CrowdinGui;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.discord.Chat;
import siongsng.rpmtwupdatemod.discord.OnChat;
import siongsng.rpmtwupdatemod.function.AFK;
import siongsng.rpmtwupdatemod.function.PackVersionCheck;
import siongsng.rpmtwupdatemod.notice.notice;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

@Mod("rpmtw_update_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RpmtwUpdateMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public final static String Mod_ID = "rpmtw_update_mod";
    private static final String PROTOCOL_VERSION = "1";

    public static Path CACHE_DIR = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16");
    public static Path PACK_NAME = CACHE_DIR.resolve("RPMTW-1.16.zip");
    public static String Update_Path = CACHE_DIR + "/Update.txt";
    public static String Latest_ver = json.ver("https://api.github.com/repos/SiongSng/ResourcePack-Mod-zh_tw/releases/latest").toString();
    public static String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1];
    public ModElements elements;
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(Mod_ID, Mod_ID),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);


    @SubscribeEvent
    public void init(final FMLClientSetupEvent e) {
        elements = new ModElements();
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Common_init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientLoad);

        MinecraftForge.EVENT_BUS.register(new FMLBusEvents(this));

        //   MinecraftForge.EVENT_BUS.register(GuiHandler.class); //設定Gui註冊
        MinecraftForge.EVENT_BUS.register(new key());  //快捷鍵註冊
        MinecraftForge.EVENT_BUS.register(new AddToken()); //AddToken指令註冊
        MinecraftForge.EVENT_BUS.register(new noticeCMD()); //noticeCMD指令註冊
        if (Configer.notice.get()) { //判斷Config
            MinecraftForge.EVENT_BUS.register(new notice()); //玩家加入事件註冊
        }
        MinecraftForge.EVENT_BUS.register(new AFK()); //掛機事件註冊
        MinecraftForge.EVENT_BUS.register(new OnChat()); //聊天事件

        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );
    }

    public RpmtwUpdateMod() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT, "rpmtw_update_mod-client.toml");
        Config.loadConfig(Config.CLIENT);
        LOGGER.info("Hello RPMTW world!");
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用RPMTW自動更新模組，請連結網路後重新啟動此模組。");
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().gameSettings.language = "zh_tw"; //將語言設定為繁體中文
        }
        new PackVersionCheck(Latest_ver, Latest_ver_n, CACHE_DIR, Update_Path, PACK_NAME);
        new Chat();
    }

    private void Common_init(FMLCommonSetupEvent event) {
        elements.getElements().forEach(element -> element.init(event));
    }

    public void clientLoad(FMLClientSetupEvent event) {
        elements.getElements().forEach(element -> element.clientLoad(event));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(elements.getEntities().stream().map(Supplier::get).toArray(EntityType[]::new));
    }

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(elements.getEnchantments().stream().map(Supplier::get).toArray(Enchantment[]::new));
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
        elements.registerSounds(event);
    }

    private static class FMLBusEvents {
        private final RpmtwUpdateMod parent;

        FMLBusEvents(RpmtwUpdateMod parent) {
            this.parent = parent;
        }

        @SubscribeEvent
        public void serverLoad(FMLServerStartingEvent event) {
            this.parent.elements.getElements().forEach(element -> element.serverLoad(event));
        }
    }
}