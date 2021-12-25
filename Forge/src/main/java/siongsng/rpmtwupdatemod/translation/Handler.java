package siongsng.rpmtwupdatemod.translation;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class Handler extends SimplePreparableReloadListener<CompletableFuture<Void>> {
    private static final Map<String, String> noLocalizedMap = new HashMap<>();
    private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");

    public boolean isKeyPress(KeyMapping key) {
        try {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
        } catch (Exception exception) {
            return false;
        }
    }

    public void registerReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(this);
    }


    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getInstance().player != null;
        if (RPMTWConfig.isTranslate.get() && playing) {
            ItemStack stack = event.getItemStack();
            String source = noLocalizedMap.getOrDefault(stack.getDescriptionId(), "無");
            event.getToolTip().add(1, new TextComponent("原文: " + source).withStyle(ChatFormatting.GRAY));
            if (press) {
                for (Component text : TranslationManager.getInstance().createToolTip(source)) {
                    event.getToolTip().add(2, text);
                }
            }
        }
    }


    @Override
    protected CompletableFuture<Void> prepare(ResourceManager manager, ProfilerFiller p_10797_) {
        return CompletableFuture.runAsync(() -> {
            for (String namespace : manager.getNamespaces()) {
                String path = "lang/en_us.json";
                try {
                    ResourceLocation identifier = new ResourceLocation(namespace, path);

                    List<Resource> resourceList = manager.getResources(identifier);

                    for (Resource resource : resourceList) {
                        final Gson GSON = new Gson();

                        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);

                        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                            String value = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                            noLocalizedMap.put(entry.getKey(), value);
                        }
                    }
                } catch (FileNotFoundException ignored) {
                } catch (Exception e) {
                    RpmtwUpdateMod.LOGGER.warn("Skipped language file: {}:{} ({})", namespace, path, e.toString());
                }
            }
        });
    }

    @Override
    protected void apply(CompletableFuture<Void> p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
    }
}
