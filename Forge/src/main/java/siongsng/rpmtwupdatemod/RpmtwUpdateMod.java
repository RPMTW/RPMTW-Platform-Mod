package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.Language;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.SocketClient;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.gui.widget.RPMButton;
import siongsng.rpmtwupdatemod.gui.widget.RPMCheckbox;
import siongsng.rpmtwupdatemod.gui.widget.TranslucentButton;
import siongsng.rpmtwupdatemod.notice.notice;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.utilities.AddPack;
import siongsng.rpmtwupdatemod.utilities.Utility;
import siongsng.rpmtwupdatemod.utilities.ping;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Mod(
        modid = RpmtwUpdateMod.MOD_ID,
        name = RpmtwUpdateMod.MOD_NAME,
        version = RpmtwUpdateMod.VERSION,
        clientSideOnly = true,
        guiFactory = "siongsng.rpmtwupdatemod.config.ConfigGuiFactory"
)

public class RpmtwUpdateMod {

    public static final String MOD_ID = "rpmtw_update_mod";
    public static final String MOD_NAME = "RPMTW Update Mod";
    public static final String VERSION = "1.2.9";
    public final static String PackDownloadUrl =
            Objects.equals(Locale.getDefault().getISO3Country(), "CHN") ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.12/RPMTW-1.12.zip" :
                    "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.12/RPMTW-1.12.zip";

    //    @Mod.Instance(MOD_ID)
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        LOGGER.info("Hello RPMTW world!");
        if (!ping.isConnect()) { //判斷是否有網路
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }

        new AddPack(); //新增翻譯資源包

        /*
        將語言設定為繁體中文
         */
        Minecraft mc = Minecraft.getMinecraft();
        if (RPMTWConfig.isChinese) {
            mc.getLanguageManager().setCurrentLanguage(new Language("zh_tw", "TW", "繁體中文", false));
            mc.gameSettings.language = "zh_tw";
            mc.gameSettings.saveOptions();
        }

        try {
            if (!RPMTWConfig.Token.equals("")) {
                new TokenCheck().Check(RPMTWConfig.Token); //開始檢測登入權杖
            }
        } catch (IOException e) {
            LOGGER.error("檢測權杖時發生未知錯誤：" + e);
        }


        SocketClient.GetMessage();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new RPMKeyBinding());
        MinecraftForge.EVENT_BUS.register(new Handler());
        MinecraftForge.EVENT_BUS.register(new notice());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void screenEvent(GuiScreenEvent.InitGuiEvent event) {
        GuiScreen screen = event.getGui();
        List<GuiButton> buttons = event.getButtonList();
        int scaledWidth = screen.width;
        int scaledHeight = screen.height;

        boolean showContributor = screen instanceof GuiIngameMenu || screen instanceof GuiInventory || screen instanceof GuiContainerCreative || screen instanceof GuiOptions;
        // 翻譯貢獻者按鈕
        if (showContributor && RPMTWConfig.contributorButton && RPMTWConfig.isChinese) {
            int y = (int) (scaledHeight / 1.155);

            if (!(screen instanceof GuiIngameMenu)) {
                y += 10;
            }

            GuiButtonImage rpmtwLogo = new GuiButtonImage(0, scaledWidth / 80, y + 2, 15, 15, 15, 15, 0, new ResourceLocation(RpmtwUpdateMod.MOD_ID, "textures/rpmtw_logo.png"));

            GuiButton buttonWidget = new RPMButton(scaledWidth / 80 - 2, y, 20, 20, "", () -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.rpmtw.com/Contributor"));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }, "查看 RPMTW 翻譯貢獻者");

            buttons.add(buttonWidget);
            buttons.add(rpmtwLogo);
        } else if (screen instanceof GuiChat && RPMTWConfig.cosmicChatButton) {
            try {
                GuiChat screenChat = (GuiChat) screen;
                Field f;
                try {
                    f = screenChat.getClass().getDeclaredField("field_146415_a");
                } catch (Exception e) {
                    f = screenChat.getClass().getDeclaredField("inputField");
                }
                f.setAccessible(true);

                GuiTextField textField = (GuiTextField) f.get(screenChat);
                if (textField != null) {
                    TranslucentButton translucentButton = new TranslucentButton(scaledWidth - 185, scaledHeight - 40, 90, 20, "發送訊息至宇宙通訊", () -> Utility.openCosmicChatScreen(textField.getText()), "發送訊息至浩瀚的宇宙中，與其他星球的生物交流"
                    );

                    RPMCheckbox checkbox = new RPMCheckbox(scaledWidth - 90, scaledHeight - 35, "接收宇宙通訊", RPMTWConfig.cosmicChat, (checked -> {
                        RPMTWConfig.cosmicChat = checked;
                        ConfigManager.sync(RpmtwUpdateMod.MOD_ID, Config.Type.INSTANCE);
                    }), "接收來自其他星球的訊息");

                    buttons.add(translucentButton);
                    buttons.add(checkbox);
                }
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.error("新增宇宙通訊發送按鈕時發生未知錯誤" + e);
            }
        }
        event.setButtonList(buttons);
    }

}
