package siongsng.rpmtwupdatemod;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModElements {
    public static Map<ResourceLocation, net.minecraft.util.SoundEvent> sounds = new HashMap<>();
    public final List<ModElement> elements = new ArrayList<>();
    public final List<Supplier<Block>> blocks = new ArrayList<>();
    public final List<Supplier<Item>> items = new ArrayList<>();
    public final List<Supplier<EntityType<?>>> entities = new ArrayList<>();
    public final List<Supplier<Enchantment>> enchantments = new ArrayList<>();
    private int messageID = 0;

    public ModElements() {
        try {
            ModFileScanData modFileInfo = ModList.get().getModFileById("rpmtw_update_mod").getFile().getScanResult();
            Set<ModFileScanData.AnnotationData> annotations = modFileInfo.getAnnotations();
            for (ModFileScanData.AnnotationData annotationData : annotations) {
                if (annotationData.getAnnotationType().getClassName().equals(ModElement.Tag.class.getName())) {
                    Class<?> clazz = Class.forName(annotationData.getClassType().getClassName());
                    if (clazz.getSuperclass() == ModElements.ModElement.class)
                        elements.add((ModElements.ModElement) clazz.getConstructor(this.getClass()).newInstance(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(elements);
        elements.forEach(ModElements.ModElement::initElements);
    }

    public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
        for (Map.Entry<ResourceLocation, net.minecraft.util.SoundEvent> sound : sounds.entrySet())
            event.getRegistry().register(sound.getValue().setRegistryName(sound.getKey()));
    }

    public <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder,
                                      BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        RpmtwUpdateMod.PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    public List<ModElement> getElements() {
        return elements;
    }

    public List<Supplier<Block>> getBlocks() {
        return blocks;
    }

    public List<Supplier<Item>> getItems() {
        return items;
    }

    public List<Supplier<EntityType<?>>> getEntities() {
        return entities;
    }

    public List<Supplier<Enchantment>> getEnchantments() {
        return enchantments;
    }

    public static class ModElement implements Comparable<ModElement> {
        protected final ModElements elements;
        protected final int sortid;

        public ModElement(ModElements elements, int sortid) {
            this.elements = elements;
            this.sortid = sortid;
        }

        public void initElements() {
        }

        public void init(FMLCommonSetupEvent event) {
        }

        public void serverLoad(FMLServerStartingEvent event) {
        }

        @OnlyIn(Dist.CLIENT)
        public void clientLoad(FMLClientSetupEvent event) {
        }

        @Override
        public int compareTo(ModElement other) {
            return this.sortid - other.sortid;
        }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface Tag {
        }
    }
}
