package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
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

@Mixin(TranslationStorage.class)
public class ClientLanguageMixin {


    @Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;)Lnet/minecraft/client/resource/language/TranslationStorage;", at = @At("HEAD"))
    private static void loadFrom(ResourceManager resourceManager, List<LanguageDefinition> list, CallbackInfoReturnable<TranslationStorage> cir) {
        UntranslatedLangChecker.loadLang = true;
    }

    @Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;)Lnet/minecraft/client/resource/language/TranslationStorage;", at = @At("RETURN"))
    private static void loadFrom2(ResourceManager resourceManager, List<LanguageDefinition> list, CallbackInfoReturnable<TranslationStorage> cir) {

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

    @Inject(method = "load(Ljava/util/List;Ljava/util/Map;)V", at = @At("HEAD"))
    private static void appendFrom(List<Resource> list, Map<String, String> map, CallbackInfo ci) {
        UntranslatedLangChecker.loadList = list;
        UntranslatedLangChecker.loadCont = 0;
        if (UntranslatedLangChecker.loadLangList == null)
            UntranslatedLangChecker.loadLangList = new HashMap<>();
    }
}
