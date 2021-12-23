package siongsng.rpmtwupdatemod.translation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import siongsng.rpmtwupdatemod.Register.RPMKeyBinding;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Handler implements SimpleSynchronousResourceReloadListener {
    private static final Map<String, String> noLocalizedMap = new HashMap<>();
    private static final Pattern TOKEN_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");

    public static boolean isKeyPress(KeyBinding key) {
        try {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyCodeAccessor) key).fabric_getBoundKey().getCode());
        } catch (Exception exception) {
            return false;
        }
    }

    public Handler() {
        ItemTooltipCallback.EVENT.register(Handler::onTooltip);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
    }

    public static void onTooltip(ItemStack itemStack, TooltipContext tooltipFlag, List<Text> list) {
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = MinecraftClient.getInstance().player != null;

        if (playing && RPMTWConfig.getConfig().isTranslate) {
            String source = noLocalizedMap.getOrDefault(itemStack.getTranslationKey(), "無");
            list.add(1, new LiteralText("原文: " + source).formatted(Formatting.GRAY));
            if (press) {
                for (Text text : TranslationManager.getInstance().createToolTip(source)) {
                    list.add(2, text);
                }
            } else {
                list.add(2, new LiteralText("按下 " + RPMKeyBinding.translate.getBoundKeyLocalizedText().asString() + " 後將物品機器翻譯為中文"));
            }
        }

    }


    @Override
    public void reload(ResourceManager manager) {
        for (String namespace : manager.getAllNamespaces()) {
            String path = "lang/en_us.json";
            try {
                Identifier identifier = new Identifier(namespace, path);

                List<Resource> resourceList = manager.getAllResources(identifier);

                for (Resource resource : resourceList) {
                    final Gson GSON = new Gson();

                    JsonObject jsonObject = GSON.fromJson(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);

                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        String value = TOKEN_PATTERN.matcher(JsonHelper.asString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                        noLocalizedMap.put(entry.getKey(), value);
                    }
                }
            } catch (FileNotFoundException ignored) {
            } catch (Exception e) {
                RpmtwUpdateMod.LOGGER.warn("Skipped language file: {}:{} ({})", namespace, path, e.toString());
            }
        }
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("rpmtw_update_mod");
    }
}
