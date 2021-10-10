package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import siongsng.rpmtwupdatemod.CosmicChat.GetMessage;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.TokenCheck;
import siongsng.rpmtwupdatemod.crowdin.key;
import siongsng.rpmtwupdatemod.function.SendMsg;

import java.util.Locale;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpmtw_update_mod");
    public final static String Mod_ID = "rpmtw_update_mod";
    public final static String PackDownloadUrl =
            Objects.equals(Locale.getDefault().getISO3Country(), "CHN") ? "https://github.com.cnpmjs.org/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip" :
                    "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip";

    public RpmtwUpdateMod() {
        if (!ping.isConnect()) {
            LOGGER.error("你目前處於無網路狀態，因此無法使用 RPMTW 翻譯自動更新模組，請連結網路後重新啟動此模組。");
        }

    }

    @Override
    public void onInitializeClient() {
        key.onInitializeClient(); //註冊快捷鍵
        
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(Mod_ID, "rpmtw1.16");
            }

			@Override
			public void reload(ResourceManager manager) {
				if(key.updateLock) {
					key.updateLock = false;
					SendMsg.send("§b處理完成。");	
				}	
			}
        });
        
        LOGGER.info("Hello RPMTW world!");

        if (!Configer.getConfig().Token.equals("")) { //如果Token不是空的
            new TokenCheck().Check(Configer.getConfig().Token); //開始檢測
        }
        if (Configer.getConfig().isChat) {
            new GetMessage();
        }
    }

}