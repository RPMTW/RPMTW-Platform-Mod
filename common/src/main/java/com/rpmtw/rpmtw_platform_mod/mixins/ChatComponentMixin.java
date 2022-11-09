package com.rpmtw.rpmtw_platform_mod.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rpmtw.rpmtw_api_client.models.universe_chat.UniverseChatMessage;
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.gui.widgets.UniverseChatComponent;
import com.rpmtw.rpmtw_platform_mod.util.ChatComponentData;
import com.rpmtw.rpmtw_platform_mod.util.Util;
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
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.List;

@Mixin(value = ChatComponent.class, priority = 100)
@Environment(EnvType.CLIENT)
public class ChatComponentMixin {
    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;
    @Shadow
    @Final
    private List<GuiMessage> allMessages;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
    public void addMessage(Component component, CallbackInfo ci) {
        List<Component> siblings = component.getSiblings();
        UniverseChatComponent chatComponent = (UniverseChatComponent) siblings.stream().filter(sibling -> sibling instanceof UniverseChatComponent).findFirst().orElse(null);

        if (chatComponent != null) {
            String avatarUrl = chatComponent.getUniverseChatMessage().getAvatarUrl();

            if (avatarUrl != null && !ChatComponentData.INSTANCE.isAvatarCached(avatarUrl)) {
                loadAvatar(avatarUrl);
            }
        }
    }


    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", index = 2)
    public float moveTheText(PoseStack poseStack, FormattedCharSequence formattedCharSequence, float f, float y, int color) {
        UniverseChatComponent chatComponent = getLastComponent();
        if (chatComponent == null) return 0.0F;

        ChatComponentData.INSTANCE.setLastY((int) y);
        ChatComponentData.INSTANCE.setLastOpacity((((color >> 24) + 256) % 256) / 255f);

        return ChatComponentData.offset;
    }


    @ModifyArg(at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", index = 0)
    public int getLastMessage(int index) {
        ChatComponentData.INSTANCE.setLastMessageIndex(index);
        return index;
    }


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I", ordinal = 0), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V")
    public void render(PoseStack matrixStack, int i, CallbackInfo ci) {
        try {
            UniverseChatComponent chatComponent = getLastComponent();
            if (chatComponent == null) return;

            UniverseChatMessage message = chatComponent.getUniverseChatMessage();
            ResourceLocation location = ChatComponentData.INSTANCE.getAvatarCache().getOrDefault(message.getAvatarUrl(), null);

            if (location == null) return;
            RenderSystem.setShaderColor(1, 1, 1, ChatComponentData.INSTANCE.getLastOpacity());
            RenderSystem.setShaderTexture(0, location);
            RenderSystem.enableBlend();
            // Draw base layer
            GuiComponent.blit(matrixStack, 0, ChatComponentData.INSTANCE.getLastY(), 8, 8, 8.0F, 8, 8, 8, 8, 8);
            // Draw hat
            GuiComponent.blit(matrixStack, 0, ChatComponentData.INSTANCE.getLastY(), 8, 8, 40.0F, 8, 8, 8, 8, 8);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
        } catch (Exception e) {
            RPMTWPlatformMod.LOGGER.warn("Rending universe chat component failed\n" + e);
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/StringSplitter;componentStyleAtWidth(Lnet/minecraft/util/FormattedCharSequence;I)Lnet/minecraft/network/chat/Style;"), method = "getClickedComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;", index = 1)
    public int correctClickPosition(int x) {
        UniverseChatComponent chatComponent = getLastComponent();
        if (chatComponent == null) return x;

        return x - ChatComponentData.offset;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;getWidth()I"), method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V", require = 0)
    public int fixTextOverflow(ChatComponent chatHud) {
        UniverseChatComponent chatComponent = getLastComponent();
        if (chatComponent == null) return chatHud.getWidth();

        return chatHud.getWidth() - ChatComponentData.offset;
    }

    private void loadAvatar(String url) {
        String urlHash = DigestUtils.md5Hex(url);

        ResourceLocation location = new ResourceLocation("rpmtw_platform_mod", "universe_chat_avatars_" + urlHash);
        Thread thread = new Thread(null, () -> {
            @Nullable NativeImage nativeImage = null;

            try {
                URL uri = new URL(url);
                File file = Util.INSTANCE.getUniverseChatAvatarLocation(urlHash);

                BufferedImage image = convertToBufferedImage(ImageIO.read(uri.openConnection().getInputStream()).getScaledInstance(8, 8, Image.SCALE_SMOOTH));
                ImageIO.write(image, "png", file);
                file.deleteOnExit();

                RPMTWPlatformMod.LOGGER.info("Loading universe chat avatar from " + file.getAbsolutePath());

                nativeImage = NativeImage.read(Files.newInputStream(file.toPath()));
            } catch (IOException e) {
                RPMTWPlatformMod.LOGGER.error("Exception downloading universe chat avatar", e);
            }

            if (nativeImage != null) {
                TextureManager manager = Minecraft.getInstance().getTextureManager();
                manager.register(location, new DynamicTexture(nativeImage));

                ChatComponentData.INSTANCE.getAvatarCache().put(url, location);
                RPMTWPlatformMod.LOGGER.info("Loaded universe chat avatar for " + url);
            }
        }, "UniverseChatAvatarLoader");
        thread.start();
    }

    private BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    @Nullable
    private UniverseChatComponent getLastComponent() {
        try {
            GuiMessage.Line line = trimmedMessages.get(ChatComponentData.INSTANCE.getLastMessageIndex());
            GuiMessage guiMessage = allMessages.stream().filter(msg -> msg.addedTime() == line.addedTime()).findFirst().orElse(null);
            if (guiMessage == null) return null;

            List<Component> siblings = guiMessage.content().getSiblings();

            return (UniverseChatComponent) siblings.stream().filter(sibling -> sibling instanceof UniverseChatComponent).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}