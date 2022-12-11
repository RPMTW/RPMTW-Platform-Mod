///*
//Credit: https://github.com/kappa-maintainer/PRP
// */
//package com.rpmtw.rpmtw_platform_mod.mixins.forge;
//
//import com.google.common.base.Preconditions;
//import com.google.gson.JsonElement;
//import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod;
//import net.minecraft.client.Minecraft;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.packs.resources.Resource;
//import net.minecraft.server.packs.resources.ResourceManager;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import vazkii.patchouli.client.book.BookContentClasspathLoader;
//import vazkii.patchouli.client.book.BookContentLoader;
//import vazkii.patchouli.client.book.BookContentsBuilder;
//import vazkii.patchouli.common.book.Book;
//import vazkii.patchouli.common.book.BookRegistry;
//
//import javax.annotation.Nullable;
//import java.io.IOException;
//import java.io.UncheckedIOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Mixin(value = BookContentClasspathLoader.class, remap = false)
//public class MixinBookContentClasspathLoader {
//    @Inject(at = @At("HEAD"), method = "findFiles")
//    private void findFiles(Book book, String dir, List<ResourceLocation> list, CallbackInfo ci) {
//        String prefix = String.format("%s/%s/%s/%s", BookRegistry.BOOKS_LOCATION, book.id.getPath(), BookContentsBuilder.DEFAULT_LANG, dir);
//        Map<ResourceLocation, Resource> files = Minecraft.getInstance().getResourceManager().listResources(prefix, p -> p.getPath().endsWith(".json"));
//
//        files.keySet().stream()
//                .distinct()
//                .filter(file -> file.getNamespace().equals(book.id.getNamespace()))
//                .map(file -> {
//                    // caller expects list to contain logical id's, not file paths.
//                    // we end up going from path -> id -> back to path, but it's okay as a transitional measure
//                    Preconditions.checkArgument(file.getPath().startsWith(prefix));
//                    Preconditions.checkArgument(file.getPath().endsWith(".json"));
//                    String newPath = file.getPath().substring(prefix.length(), file.getPath().length() - ".json".length());
//                    // Vanilla expects `prefix` above to not have a trailing slash, so we
//                    // have to remove it ourselves from the path
//                    if (newPath.startsWith("/")) {
//                        newPath = newPath.substring(1);
//                    }
//                    return new ResourceLocation(file.getNamespace(), newPath);
//                })
//                .forEach(list::add);
//
//    }
//
//    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
//    private void loadJson(Book book, ResourceLocation location, @Nullable ResourceLocation fallback, CallbackInfoReturnable<JsonElement> callback) {
//        RPMTWPlatformMod.LOGGER.debug("[Patchouli] Loading {}", location);
//        ResourceManager manager = Minecraft.getInstance().getResourceManager();
//        try {
//            Optional<Resource> resource = manager.getResource(location);
//            Optional<Resource> fallbackResource = manager.getResource(location);
//
//            if (resource.isPresent()) {
//                callback.setReturnValue(BookContentLoader.streamToJson(resource.get().open()));
//            } else if (fallbackResource.isPresent()) {
//                callback.setReturnValue(BookContentLoader.streamToJson(fallbackResource.get().open()));
//            }
//        } catch (IOException ex) {
//            throw new UncheckedIOException(ex);
//        }
//    }
//}