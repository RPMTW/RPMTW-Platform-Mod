// @file:JVMName("ChatComponentMixin")
package com.rpmtw.rpmtw_platform_mod.mixins

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.gui.widgets.CosmicChatComponent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.GuiMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FormattedCharSequence
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.ModifyArg
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import javax.imageio.ImageIO

@Suppress("UNUSED_PARAMETER")
@Mixin(ChatComponent::class)
@Environment(EnvType.CLIENT)
class ChatComponentMixin {
    @field:Shadow
    @field:Final
    private val minecraft: Minecraft? = null

    @field:Shadow
    @field:Final
    private val trimmedMessages: List<GuiMessage<FormattedCharSequence>>? = null

    @field:Shadow
    @field:Final
    private val allMessages: List<GuiMessage<Component>?>? = null

    @Inject(method = ["addMessage(Lnet/minecraft/network/chat/Component;IIZ)V"], at = [At("HEAD")])
    fun addMessage(component: Component, i: Int, j: Int, bl: Boolean, ci: CallbackInfo?) {
        val siblings = component.siblings
        val chatComponent: CosmicChatComponent? =
            siblings.stream().filter { sibling: Component? -> sibling is CosmicChatComponent }
                .findFirst().orElse(null) as CosmicChatComponent?

        if (chatComponent != null) {
            val (_, _, _, _, avatarUrl) = chatComponent.cosmicChatMessage
            val avatarMap: Map<String, ResourceLocation?> = CosmicChatData.avatarCache
            if (!avatarMap.containsKey(avatarUrl)) {
                loadImage(avatarUrl)
            }
        }
    }

    @ModifyArg(
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I",
            ordinal = 0
        ), method = ["render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"], index = 2
    )
    fun moveTheText(
        poseStack: PoseStack,
        formattedCharSequence: FormattedCharSequence,
        f: Float,
        y: Float,
        color: Int
    ): Float {
        CosmicChatData.lastY = y.toInt()
        CosmicChatData.lastOpacity = ((color shr 24) + 256) % 256 / 255f // haha yes
        return CosmicChatData.offset.toFloat()
    }

    @ModifyArg(
        at = At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0),
        method = ["render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"],
        index = 0
    )
    fun getLastMessage(index: Int): Int {
        CosmicChatData.lastMessageIndex = index
        return index
    }

    @Inject(
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/util/FormattedCharSequence;FFI)I",
            ordinal = 0
        )], method = ["render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"]
    )
    fun render(matrixStack: PoseStack, i: Int, ci: CallbackInfo?) {
        try {
            val guiMessage = trimmedMessages!![CosmicChatData.lastMessageIndex]
            val component = allMessages!!.stream().filter { msg: GuiMessage<Component>? -> msg!!.id == guiMessage.id }
                .findFirst().orElse(null)
                ?: return
            val siblings = component.message.siblings
            val chatComponent: CosmicChatComponent =
                siblings.stream().filter { sibling: Component? -> sibling is CosmicChatComponent }
                    .findFirst().orElse(null) as CosmicChatComponent? ?: return

            val (_, _, _, _, avatarUrl) = chatComponent.cosmicChatMessage
            val location = CosmicChatData.avatarCache.getOrDefault(avatarUrl, null) ?: return
            RenderSystem.setShaderColor(1f, 1f, 1f, CosmicChatData.lastOpacity)
            RenderSystem.setShaderTexture(0, location)
            RenderSystem.enableBlend()
            // Draw base layer
            GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 8.0f, 8f, 8, 8, 8, 8)
            // Draw hat
            GuiComponent.blit(matrixStack, 0, CosmicChatData.lastY, 8, 8, 40.0f, 8f, 8, 8, 8, 8)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            RenderSystem.disableBlend()
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.info("Rending chat component failed\n$e")
        }
    }

    @ModifyArg(
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/StringSplitter;componentStyleAtWidth(Lnet/minecraft/util/FormattedCharSequence;I)Lnet/minecraft/network/chat/Style;"
        ), method = ["getClickedComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;"], index = 1
    )
    fun correctClickPosition(x: Int): Int {
        return x - CosmicChatData.offset
    }

    @Redirect(
        at = At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;getWidth()I"),
        method = ["addMessage(Lnet/minecraft/network/chat/Component;IIZ)V"]
    )
    fun fixTextOverflow(chatHud: ChatComponent?): Int {
        return ChatComponent.getWidth(minecraft!!.options.chatWidth) - CosmicChatData.offset
    }
}

private fun loadImage(url: String) {
    val location =
        ResourceLocation("rpmtw_platform_mod", "cosmic_chat_user_avatar/" + url.replace("https://", ""))
    val thread = Thread(null, {
        var nativeImage: NativeImage? = null
        try {
            val uri = URL(url)
            val file = File(
                System.getProperty("user.home") + "/.rpmtw/cosmic_chat_user_avatar/" + url.replace(
                    "https://",
                    ""
                ) + ".png"
            )
            val image = convertToBufferedImage(
                ImageIO.read(uri.openConnection().getInputStream()).getScaledInstance(8, 8, Image.SCALE_SMOOTH)
            )
            ImageIO.write(image, "png", file)
            file.deleteOnExit()
            RPMTWPlatformMod.LOGGER.info("Loading image from " + file.absolutePath)
            nativeImage = NativeImage.read(Files.newInputStream(file.toPath()))
        } catch (e: IOException) {
            RPMTWPlatformMod.LOGGER.error("Exception reading image", e)
        }
        if (nativeImage != null) {
            val manager = Minecraft.getInstance().textureManager
            manager.register(location, DynamicTexture(nativeImage))
            CosmicChatData.avatarCache[url] = location
            RPMTWPlatformMod.LOGGER.info("Loaded image for $url")
        }
    }, "CosmicChatAvatarLoader")
    thread.start()
}

private fun convertToBufferedImage(image: Image): BufferedImage {
    val newImage = BufferedImage(
        image.getWidth(null), image.getHeight(null),
        BufferedImage.TYPE_4BYTE_ABGR
    )
    val g = newImage.createGraphics()
    g.drawImage(image, 0, 0, null)
    g.dispose()
    return newImage
}

private object CosmicChatData {
    const val offset = 10
    var lastY = 0
    var lastMessageIndex = 0
    var lastOpacity = 0f
    var avatarCache: MutableMap<String, ResourceLocation?> = HashMap()
}