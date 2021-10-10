package siongsng.rpmtwupdatemod.mixins;

import com.google.gson.JsonElement;
import net.minecraft.resource.Resource;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.translation.UntranslatedLangChecker;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(Language.class)
public class LanguageMixin {

    @ModifyVariable(method = "load", ordinal = 0, at = @At("STORE"))
    private static Map.Entry<String, JsonElement> loadFromJson(Map.Entry<String, JsonElement> entry) {
        if (UntranslatedLangChecker.loadLang) {
            List<Resource> loadList = UntranslatedLangChecker.loadList;
            String loadName = loadList != null ? loadList.get(UntranslatedLangChecker.loadCont).getId().getPath() : null;
            if (loadName != null) {
                if (!UntranslatedLangChecker.loadLangList.containsKey(loadName))
                    UntranslatedLangChecker.loadLangList.put(loadName, new HashMap<>());
                UntranslatedLangChecker.loadLangList.get(loadName).put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return entry;
    }

    @Inject(method = "load", at = @At("TAIL"))
    private static void loadFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer, CallbackInfo ci) {
        if (UntranslatedLangChecker.loadLang)
            UntranslatedLangChecker.loadCont++;
    }
}
