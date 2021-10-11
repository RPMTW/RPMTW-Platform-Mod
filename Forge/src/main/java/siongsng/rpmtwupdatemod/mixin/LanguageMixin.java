package siongsng.rpmtwupdatemod.mixin;

import com.google.gson.JsonElement;
import net.minecraft.resources.IResource;
import net.minecraft.util.text.LanguageMap;
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

@Mixin(LanguageMap.class)
public class LanguageMixin {

    @ModifyVariable(method = "func_240593_a_", ordinal = 0, at = @At("STORE"), remap = false)
    private static Map.Entry<String, JsonElement> loadFromJson(Map.Entry<String, JsonElement> entry) {
        if (UntranslatedLangChecker.loadLang) {
            List<IResource> loadList = UntranslatedLangChecker.loadList;
            String loadName = loadList != null ? loadList.get(UntranslatedLangChecker.loadCont).getLocation().getPath() : null;
            if (loadName != null) {
                if (!UntranslatedLangChecker.loadLangList.containsKey(loadName))
                    UntranslatedLangChecker.loadLangList.put(loadName, new HashMap<>());
                UntranslatedLangChecker.loadLangList.get(loadName).put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return entry;
    }

    @Inject(method = "func_240593_a_(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V", at = @At("TAIL"), remap = false)
    private static void loadFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer, CallbackInfo ci) {
        if (UntranslatedLangChecker.loadLang)
            UntranslatedLangChecker.loadCont++;
    }
}
