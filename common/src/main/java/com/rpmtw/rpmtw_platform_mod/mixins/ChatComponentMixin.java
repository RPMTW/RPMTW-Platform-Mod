package com.rpmtw.rpmtw_platform_mod.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage;
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.gui.widgets.CosmicChatComponent;
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(ChatComponent.class)
@Environment(EnvType.CLIENT)
public class ChatComponentMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private List<GuiMessage<FormattedCharSequence>> trimmedMessages;
    @Shadow
    @Final
    private List<GuiMessage<Component>> allMessages;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V", at = @At("HEAD"))
    public void addMessage(Component component, int i, int j, boolean bl, CallbackInfo ci) {
        List<Component> siblings = component.getSiblings();
        CosmicChatComponent chatComponent = (CosmicChatComponent) siblings.stream().filter(sibling -> sibling instanceof CosmicChatComponent).findFirst().orElse(null);

        if (chatComponent != null) {
            CosmicChatMessage message = chatComponent.getCosmicChatMessage();
            String avatarUrl = message.getAvatarUrl();
            Map<String, ResourceLocation> avatarMap = CosmicChatData.avatarCache;
            if (!avatarMap.containsKey(avatarUrl)) {
                loadImage(avatarUrl);
            }
        }
    }


    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", index = 2)
    public float moveTheText(PoseStack poseStack, FormattedCharSequence formattedCharSequence, float f, float y, int color) {
        CosmicChatData.lastY = (int) y;
        CosmicChatData.lastOpacity = (((color >> 24) + 256) % 256) / 255f; // haha yes
        return CosmicChatData.offset;
    }


    @ModifyArg(at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", index = 0)
    public int getLastMessage(int index) {
        CosmicChatData.lastMessageIndex = index;
        return index;
    }


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V")
    public void render(PoseStack matrixStack, int i, CallbackInfo ci) {
        try {
            GuiMessage<FormattedCharSequence> guiMessage = trimmedMessages.get(CosmicChatData.lastMessageIndex);
            GuiMessage<Component> component = allMessages.stream().filter(msg -> msg.getId() == guiMessage.getId()).findFirst().orElse(null);
            if (component == null) return;

            List<Component> siblings = component.getMessage().getSiblings();
            CosmicChatComponent chatComponent = (CosmicChatComponent) siblings.stream().filter(sibling -> sibling instanceof CosmicChatComponent).findFirst().orElse(null);
            if (chatComponent == null) return;

            CosmicChatMessage message = chatComponent.getCosmicChatMessage();
            ResourceLocation location = CosmicChatData.avatarCache.getOrDefault(message.getAvatarUrl(), null);

            if (location == null) return;
            RenderSystem.setShaderColor(1, 1, 1, CosmicChatData.lastOpacity);
            RenderSystem.setShaderTexture(0, location);
            RenderSystem.enableBlend();
            // Draw base layer
            GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 8.0F, 8, 8, 8, 8, 8);
            // Draw hat
            GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 40.0F, 8, 8, 8, 8, 8);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
        } catch (Exception e) {
            RPMTWPlatformMod.LOGGER.info("Rending chat component failed\n" + e);
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/StringSplitter;componentStyleAtWidth(Lnet/minecraft/util/FormattedCharSequence;I)Lnet/minecraft/network/chat/Style;"), method = "getClickedComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;", index = 1)
    public int correctClickPosition(int x) {
        return x - CosmicChatData.offset;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;getWidth()I"), method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V")
    public int fixTextOverflow(ChatComponent chatHud) {
        return ChatComponent.getWidth(minecraft.options.chatWidth) - CosmicChatData.offset;
    }

    private void loadImage(String url) {
        ResourceLocation location = new ResourceLocation("rpmtw_platform_mod", "cosmic_chat_user_avatar/" + url.replace("https://", ""));
        Thread thread = new Thread(null, () -> {
            @Nullable NativeImage nativeImage = null;

            try {
                URL uri = new URL(url);
                File file = Utilities.INSTANCE.getFileLocation("/.rpmtw/cosmic_chat_user_avatar/" + url.replace("https://", "") + ".png");

                BufferedImage image = convertToBufferedImage(ImageIO.read(uri.openConnection().getInputStream()).getScaledInstance(8, 8, Image.SCALE_SMOOTH));
                ImageIO.write(image, "png", file);
                file.deleteOnExit();

                RPMTWPlatformMod.LOGGER.info("Loading image from " + file.getAbsolutePath());

                nativeImage = NativeImage.read(Files.newInputStream(file.toPath()));
            } catch (IOException e) {
                RPMTWPlatformMod.LOGGER.error("Exception reading image", e);
            }

            if (nativeImage != null) {
                TextureManager manager = Minecraft.getInstance().getTextureManager();
                manager.register(location, new DynamicTexture(nativeImage));

                CosmicChatData.avatarCache.put(url, location);
                RPMTWPlatformMod.LOGGER.info("Loaded image for " + url);
            }
        }, "CosmicChatAvatarLoader");
        thread.start();
    }

    private static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}

class CosmicChatData {
    static int offset = 10;
    static int lastY = 0;
    static int lastMessageIndex = 0;
    static float lastOpacity = 0f;
    static Map<String, ResourceLocation> avatarCache = new HashMap<>();
}