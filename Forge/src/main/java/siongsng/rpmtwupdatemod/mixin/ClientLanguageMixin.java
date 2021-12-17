package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.translation.Handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Mixin(Locale.class)
public class ClientLanguageMixin {
    @Inject(method = "loadLocaleDataFiles", at = @At("RETURN"), remap = false)
    private void loadFrom2(IResourceManager resourceManager, List<String> languageList, CallbackInfo ci) {
        ArrayList<String> langList = new ArrayList<>();
        langList.add("en_us");
        langList.add("en_US");

        for (String lang : langList) {
            String s1 = String.format("lang/%s.lang", lang);

            for (String modID : resourceManager.getResourceDomains()) {
                try {
                    List<IResource> resources = resourceManager.getAllResources(new ResourceLocation(modID, s1));
                    for (IResource resource : resources) {
                        Properties props = new Properties();
                        props.load(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
                        for (Map.Entry<Object, Object> e : props.entrySet()) {
                            Handler.addNoLocalizedMap((String) e.getKey(), (String) e.getValue());
                        }
                        props.clear();
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }


}
