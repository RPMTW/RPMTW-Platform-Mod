/*
Credit: https://github.com/kappa-maintainer/PRP
 */
package com.rpmtw.rpmtw_platform_mod.mixins.forge;

import com.google.common.base.Preconditions;
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookContentClasspathLoader;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;

@Mixin(value = BookContentClasspathLoader.class, remap = false)
public class MixinBookContentClasspathLoader {
    @Inject(at = @At("HEAD"), method = "findFiles")
    private void findFiles(Book book, String dir, List<ResourceLocation> list, CallbackInfo ci) {
        String prefix = String.format("%s/%s/%s/%s", BookRegistry.BOOKS_LOCATION, book.id.getPath(), BookContentsBuilder.DEFAULT_LANG, dir);
        Collection<ResourceLocation> files = Minecraft.getInstance().getResourceManager().listResources(prefix, p -> p.endsWith(".json"));

        files.stream().distinct().filter(file -> file.getNamespace().equals(book.id.getNamespace())).map(file -> {
            // caller expects list to contain logical id's, not file paths.
            // we end up going from path -> id -> back to path, but it's okay as a transitional measure
            Preconditions.checkArgument(file.getPath().startsWith(prefix));
            Preconditions.checkArgument(file.getPath().endsWith(".json"));
            String newPath = file.getPath().substring(prefix.length(), file.getPath().length() - ".json".length());
            // Vanilla expects `prefix` above to not have a trailing slash, so we
            // have to remove it ourselves from the path
            if (newPath.startsWith("/")) {
                newPath = newPath.substring(1);
            }
            return new ResourceLocation(file.getNamespace(), newPath);
        }).forEach(list::add);
    }

    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(Book book, ResourceLocation file, @Nullable ResourceLocation fallback, CallbackInfoReturnable<InputStream> callback) {
        RPMTWPlatformMod.LOGGER.debug("[Patchouli] Loading {}", file);
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        try {
            if (manager.hasResource(file)) {
                callback.setReturnValue(manager.getResource(file).getInputStream());
            } else if (fallback != null && manager.hasResource(fallback)) {
                callback.setReturnValue(manager.getResource(fallback).getInputStream());
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}