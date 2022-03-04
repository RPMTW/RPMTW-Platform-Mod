package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NamespaceResourceManager.class)
public interface NamespaceResourceManagerAccessor {
    @Accessor
    List<ResourcePack> getPackList();
}
