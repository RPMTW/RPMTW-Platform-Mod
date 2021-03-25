package siongsng.rpmtwupdatemod;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;

import java.util.function.Consumer;

public class PackFinder implements IPackFinder {
    @Override
    public void findPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory iFactory) {
        ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack("RPMTW-1.16.zip",
                true, () -> new FilePack(RpmtwUpdateMod.PACK_NAME.toFile()),
                iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }
}
