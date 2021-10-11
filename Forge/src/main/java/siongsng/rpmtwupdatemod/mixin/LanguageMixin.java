package siongsng.rpmtwupdatemod.mixin;

import com.google.gson.JsonElement;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.text.translation.LanguageMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.translation.UntranslatedLangChecker;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(LanguageMap.class)
public class LanguageMixin {

    @ModifyVariable(method = "parseLangFile", ordinal = 0, at = @At("STORE"), remap = false)
    private static Map.Entry<String, JsonElement> loadFromJson(Map.Entry<String, JsonElement> entry) {
        RpmtwUpdateMod.LOGGER.info("test");
        if (UntranslatedLangChecker.loadLang) {
            List<IResource> loadList = UntranslatedLangChecker.loadList;
            String loadName = loadList != null ? loadList.get(UntranslatedLangChecker.loadCont).getResourceLocation().getPath() : null;
            if (loadName != null) {
                if (!UntranslatedLangChecker.loadLangList.containsKey(loadName))
                    UntranslatedLangChecker.loadLangList.put(loadName, new HashMap<>());
                UntranslatedLangChecker.loadLangList.get(loadName).put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return entry;
    }

    @Inject(method = "parseLangFile", at = @At("TAIL"), remap = false)
    private static void loadFromJson(InputStream s1, CallbackInfoReturnable<Map<String, String>> cir) {
        RpmtwUpdateMod.LOGGER.info("test");
        if (UntranslatedLangChecker.loadLang)
            UntranslatedLangChecker.loadCont++;
    }
}
