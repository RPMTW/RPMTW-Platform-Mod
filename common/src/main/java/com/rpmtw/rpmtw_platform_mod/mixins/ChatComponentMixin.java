package com.rpmtw.rpmtw_platform_mod.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rpmtw.rpmtw_api_client.models.cosmic_chat.CosmicChatMessage;
import com.rpmtw.rpmtw_platform_mod.CosmicChatData;
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import com.rpmtw.rpmtw_platform_mod.gui.widgets.CosmicChatComponent;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

// TODO: cosmic chat user avatar image

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    private NativeImage image;
    private ResourceLocation imageLocation;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V", at = @At("HEAD"))
    public void addMessage(Component component, int i, int j, boolean bl, CallbackInfo ci) {
        List<Component> siblings = component.getSiblings();
        CosmicChatComponent chatComponent = (CosmicChatComponent) siblings.stream().filter(sibling -> sibling instanceof CosmicChatComponent).findFirst().orElse(null);

        if (chatComponent != null) {
            CosmicChatMessage message = chatComponent.getCosmicChatMessage();
            String avatarUrl = message.getAvatarUrl();
            Map<String, ResourceLocation> avatarMap = CosmicChatData.cosmicChatUserAvatar;
            if (avatarMap.containsKey(avatarUrl)) {
                imageLocation = avatarMap.get(avatarUrl);
                return;
            }
            ResourceLocation location = loadImage(avatarUrl);
            if (location == null) return;
            avatarMap.put(avatarUrl, location);
            CosmicChatData.cosmicChatUserAvatar = avatarMap;
            imageLocation = location;
        }
    }

    @ModifyArg(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I",
                    ordinal = 0
            ),
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V",
            index = 2
    )
    public float moveTheText(PoseStack poseStack, FormattedCharSequence formattedCharSequence, float f, float y, int color) {
        CosmicChatData.lastY = (int) y;
        CosmicChatData.lastOpacity = (((color >> 24) + 256) % 256) / 255f; // haha yes
        return CosmicChatData.Offset;
    }


    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I",
                    ordinal = 0
            ),
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"
    )
    public void render(PoseStack matrixStack, int i, CallbackInfo ci) {
        if (imageLocation == null) return;
        RenderSystem.setShaderColor(1, 1, 1, CosmicChatData.lastOpacity);
        minecraft.getTextureManager().bindForSetup(imageLocation);

        // draw base layer
        GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 8.0F, 8, 8, 8, 64, 64);
        // draw hat
        // GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 40.0F, 8, 8, 8, 64, 64);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    @ModifyArg(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/StringSplitter;componentStyleAtWidth(Lnet/minecraft/util/FormattedCharSequence;I)Lnet/minecraft/network/chat/Style;"
            ),
            method = "getClickedComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;",
            index = 1
    )
    public int correctClickPosition(int x) {
        return x - CosmicChatData.Offset;
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/ChatComponent;getWidth()I"
            ),
            method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V"
    )
    public int fixTextOverflow(ChatComponent chatHud) {
        return ChatComponent.getWidth(minecraft.options.chatWidth) - CosmicChatData.Offset;
    }

    @Nullable
    private ResourceLocation loadImage(String url) {
        try {
            HttpURLConnection connection = openConnection(url);
            if (connection == null) return null;
            image = NativeImage.read(connection.getInputStream());
        } catch (IOException e) {
            RPMTWPlatformMod.LOGGER.error("Exception reading image", e);
        }

        if (image == null) return null;

        image = scaleImage(image, 64, 64);
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        return manager.register("cosmic_chat_user_avatar", new DynamicTexture(image));
    }


    private NativeImage scaleImage(NativeImage image, int maxWidth, int maxHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width > maxWidth) {
            width = maxWidth;
            height = (int) (((float) image.getHeight() / (float) image.getWidth()) * (float) width);
        }

        if (height > maxHeight) {
            height = maxHeight;
            width = (int) (((float) image.getWidth() / (float) image.getHeight()) * (float) height);
        }

        NativeImage scaledImage = new NativeImage(width, height, false);
        image.resizeSubRectTo(0, 0, image.getWidth(), image.getHeight(), scaledImage);
        return scaledImage;
    }

    @Nullable
    private HttpURLConnection openConnection(String url) {
        try {
            final String USER_AGENT = "Mozilla/4.0";
            URL uri = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);
            return connection;
        } catch (IOException e) {
            RPMTWPlatformMod.LOGGER.error("Failed to open connection", e);
            return null;
        }
    }
}


