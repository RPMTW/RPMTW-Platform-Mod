package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraft.client.resources.Language;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import siongsng.rpmtwupdatemod.translation.Handler;
import siongsng.rpmtwupdatemod.translation.UntranslatedLangChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientLanguageMap.class)
public class ClientLanguageMixin {

    @Inject(method = "func_239497_a_", at = @At("HEAD"), remap = false)
    private static void loadFrom(IResourceManager resourceManager, List<Language> list, CallbackInfoReturnable<ClientLanguageMap> cir) {
        UntranslatedLangChecker.loadLang = true;
    }

    @Inject(method = "func_239497_a_", at = @At("RETURN"), remap = false)
    private static void loadFrom2(IResourceManager resourceManager, List<Language> list, CallbackInfoReturnable<ClientLanguageMap> cir) {

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

    @Inject(method = "func_239498_a_", at = @At("HEAD"), remap = false)
    private static void appendFrom(List<IResource> list, Map<String, String> map, CallbackInfo ci) {
        UntranslatedLangChecker.loadList = list;
        UntranslatedLangChecker.loadCont = 0;
        if (UntranslatedLangChecker.loadLangList == null)
            UntranslatedLangChecker.loadLangList = new HashMap<>();
    }
}
