package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.translation.UntranslatedLangChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(Locale.class)
public class ClientLanguageMixin {

    @Inject(method = "loadLocaleDataFiles", at = @At("HEAD"), remap = false)
    private static void loadFrom(IResourceManager s1, List<String> s, CallbackInfo ci) {
        RpmtwUpdateMod.LOGGER.info("test");
        UntranslatedLangChecker.loadLang = true;
    }

    @Inject(method = "loadLocaleDataFiles", at = @At("RETURN"), remap = false)
    private static void loadFrom2(IResourceManager s1, List<String> s, CallbackInfo ci) {
        RpmtwUpdateMod.LOGGER.info("test");

        Map<String, String> en_us = UntranslatedLangChecker.loadLangList.get("lang/en_us.json");

        if (en_us != null) {
            for (Map.Entry<String, Map<String, String>> entry : UntranslatedLangChecker.loadLangList.entrySet()) {
                if (entry.getKey().equals("lang/en_us.json"))
                    continue;
                for (Map.Entry<String, String> _entry : en_us.entrySet()) {
                    Handler.addNoLocalizedMap(_entry.getKey(), _entry.getValue());
                }
            }
        }


        UntranslatedLangChecker.loadList.clear();
        UntranslatedLangChecker.loadList = null;
        UntranslatedLangChecker.loadLangList.clear();
        UntranslatedLangChecker.loadLangList = null;


        UntranslatedLangChecker.loadLang = false;
    }

    @Inject(method = "loadLocaleData(Ljava/util/List;)V", at = @At("HEAD"), remap = false)
    private static void appendFrom(List<IResource> list, CallbackInfo ci) {
        UntranslatedLangChecker.loadList = list;
        UntranslatedLangChecker.loadCont = 0;
        if (UntranslatedLangChecker.loadLangList == null)
            UntranslatedLangChecker.loadLangList = new HashMap<>();
    }
}
