package siongsng.rpmtwupdatemod.translation;


import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class Handler implements ISelectiveResourceReloadListener {
    public Handler() {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
    }

    private static final Map<String, String> noLocalizedMap = new HashMap<>();

    public static void addNoLocalizedMap(String key, String value) {
        noLocalizedMap.put(key, value);
    }

    public boolean isKeyPress(KeyBinding key) {
        try {
            return Keyboard.isKeyDown(key.getKeyCode());
        } catch (Exception exception) {
            return false;
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        String source = noLocalizedMap.getOrDefault(stack.getTranslationKey(), noLocalizedMap.getOrDefault(stack.getTranslationKey() + ".name", "無"));
        event.getToolTip().add(1, new TextComponentString("原文: " + source).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
        boolean press = isKeyPress(RPMKeyBinding.translate);
        boolean playing = Minecraft.getMinecraft().player != null;
        if (RPMTWConfig.isTranslate && playing) {
            if (press) {
                for (ITextComponent text : TranslationManager.getInstance().createToolTip(source)) {
                    event.getToolTip().add(2, text.getFormattedText());
                }
            } else {
                event.getToolTip().add(2, "按下 " + RPMKeyBinding.translate.getDisplayName() + " 後將物品機器翻譯為中文");
            }
        }

    }


    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, @Nonnull Predicate<IResourceType> resourcePredicate) {
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
