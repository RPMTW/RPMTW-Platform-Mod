/*
更換Patchouli手冊載入位置
本類別程式碼來自:
https://github.com/kappa-maintainer/PRP/blob/main/LICENSE
https://github.com/CFPAOrg/I18nUpdateMod2/blob/1.16.5/src/main/java/com/github/tartaricacid/i18nupdatemod/mixin/MixinBookContents.java
*/

package siongsng.rpmtwupdatemod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import vazkii.patchouli.client.book.BookContentClasspathLoader;
import vazkii.patchouli.client.book.BookContentExternalLoader;
import vazkii.patchouli.common.book.Book;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

@Mixin(BookContentClasspathLoader.class)
public class MixinBookContents {
    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(Book book, ResourceLocation resloc, @Nullable ResourceLocation fallback, CallbackInfoReturnable<InputStream> cir) {
        RpmtwUpdateMod.LOGGER.debug("loading json from {}.", resloc);
        try {
            cir.setReturnValue(Minecraft.getInstance().getResourceManager().getResource(resloc).getInputStream());
        } catch (IOException e) {
            //no-op
        }
    }
}


@Mixin(BookContentExternalLoader.class)
class MixinContentExternalLoader {
    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(Book book, ResourceLocation resloc, @Nullable ResourceLocation fallback, CallbackInfoReturnable<InputStream> cir) {
        RpmtwUpdateMod.LOGGER.debug("loading json from {}.", resloc);
        try {
            cir.setReturnValue(Minecraft.getInstance().getResourceManager().getResource(resloc).getInputStream());
        } catch (IOException e) {
            //no-op
        }
    }
}