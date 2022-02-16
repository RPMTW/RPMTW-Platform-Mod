package com.rpmtw.rpmtw_platform_mod.mixins;

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.utilities.RPMTWConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(Minecraft.class)
public class ClientInit {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(GameConfig gameConfig, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        String langCode = Locale.getDefault().getLanguage();
        // if auto-toggle language is enabled and the user language is Chinese, set the language
        if (RPMTWConfig.get().translate.autoToggleLanguage && (langCode.contains("zh") || langCode.contains("chi"))) {
            String countryCode = Locale.getDefault().getCountry();
            LanguageManager manager = minecraft.getLanguageManager();
            if (countryCode.contains("TW") || countryCode.contains("HK")) {
                // set the language to Traditional Chinese
                manager.setSelected(new LanguageInfo("zh_tw", "TW", "繁體中文", false));
            } else if (countryCode.contains("CN")) {
                // set language to Simplified Chinese
                manager.setSelected(new LanguageInfo("zh_cn", "CN", "简体中文", false));
            }
            LanguageInfo info = manager.getSelected();
            RPMTWPlatformMod.LOGGER.info("Toggled language to " + info.getName() + " (" + info.getCode() + ")");
        }
    }
}
