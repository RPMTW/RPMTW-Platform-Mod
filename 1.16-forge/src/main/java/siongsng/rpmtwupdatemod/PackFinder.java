package siongsng.rpmtwupdatemod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import com.google.common.base.Supplier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackInfo.IFactory;
import net.minecraft.resources.ResourcePackInfo.Priority;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;

public final class PackFinder implements IPackFinder {

    private static final IMetadataSectionSerializer<PackMeta> META_SERIALIZER = new PackMetaSerializer();
    public static final PackFinder RESOUCE = new PackFinder(Type.RESOURCES);

    public static class Configuration {
        public static boolean allowDataPacks() {
            return true;
        }
    }

    private final Type type;
    private final File loaderDirectory;

    private PackFinder(Type type) {
        this.type = type;
        this.loaderDirectory = new File(type.path);
        try {
            Files.createDirectories(this.loaderDirectory.toPath());
        } catch (final IOException e) {
            RpmtwUpdateMod.LOGGER.error("初始化載入器失敗", e);
        }
    }

    @Override
    public void findPacks(Consumer<ResourcePackInfo> packs, IFactory factory) {
        if (!this.type.enabled.getAsBoolean()) {
            RpmtwUpdateMod.LOGGER.info("{} 透過config禁止載入", this.type.displayName);
            return;
        }

        for (final File packCandidate : getFilesFromDir(this.loaderDirectory)) {
            final boolean isFilePack = packCandidate.isFile() && packCandidate.getName().startsWith("rpmtw-1.16.zip");
            final boolean isFolderPack = !isFilePack && packCandidate.isDirectory() && new File(packCandidate, "pack.mcmeta").isFile();
            if (isFilePack || isFolderPack) {
                final String packName = this.type.path + "/" + packCandidate.getName();
                final Supplier<IResourcePack> pack = this.getAsPack(packCandidate);
                final PackMeta meta = getPackMeta(packName, pack);
                RpmtwUpdateMod.LOGGER.info("載入 {} {}。", this.type.displayName, packName);
                RpmtwUpdateMod.LOGGER.debug("載入 {} 使用 {}。", packName, meta);
                final ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack(packName, meta.isBuiltIn, pack, factory, meta.priority, IPackNameDecorator.PLAIN);

                if (packInfo != null) {
                    packs.accept(packInfo);
                }
            } else {
                RpmtwUpdateMod.LOGGER.error("Failed to load {} from {}. Archive packs must be zips. Folder packs must have a valid pack.mcmeta file.", this.type.displayName, packCandidate.getAbsolutePath());
            }
        }
    }

    public static PackMeta getPackMeta(String packName, Supplier<IResourcePack> pack) {
        try (IResourcePack packData = pack.get()) {
            final PackMeta meta = packData.getMetadata(META_SERIALIZER);
            if (meta != null) {
                return meta;
            }
        } catch (final IOException e) {
            RpmtwUpdateMod.LOGGER.warn("Could not load pack meta from {}!", packName);
            RpmtwUpdateMod.LOGGER.catching(e);
        }
        return new PackMeta(true, Priority.TOP, true);
    }

    private Supplier<IResourcePack> getAsPack(File file) {
        return file.isDirectory() ? () -> new FolderPack(file) : () -> new FilePack(file);
    }

    private static File[] getFilesFromDir(File file) {
        File[] files = new File[0];
        if (file == null) {
            RpmtwUpdateMod.LOGGER.error("Attempted to read from a null file.");
        } else if (!file.isDirectory()) {
            RpmtwUpdateMod.LOGGER.error("Can not read from {}. It's not a directory.", file.getAbsolutePath());
        } else {
            try {
                final File[] readFiles = file.listFiles();
                if (readFiles == null) {
                    RpmtwUpdateMod.LOGGER.error("Could not read from {} due to a system error. This is likely an issue with your computer.", file.getAbsolutePath());
                } else {
                    files = readFiles;
                }
            } catch (final SecurityException e) {
                RpmtwUpdateMod.LOGGER.error("Could not read from {}. Blocked by system level security. This is likely an issue with your computer.", file.getAbsolutePath(), e);
            }
        }
        return files;
    }

    public static enum Type {

        RESOURCES("PRMTW繁體中文資源包", System.getProperty("user.home") + "/.rpmtw/1.16", Configuration::allowDataPacks);

        final String displayName;
        final String path;
        final BooleanSupplier enabled;

        Type(String name, String path, BooleanSupplier enabled) {
            this.displayName = name;
            this.path = path;
            this.enabled = enabled;
        }
    }

    static class PackMetaSerializer implements IMetadataSectionSerializer<PackMeta> {

        @Override
        public String getSectionName() {
            return "rpmtw";
        }

        @Override
        public PackMeta deserialize(JsonObject json) {

            final boolean builtin = JSONUtils.getBoolean(json, "builtin", true);
            final String priorityName = JSONUtils.getString(json, "priority", "top").toLowerCase();
            final Priority priority = "top".equalsIgnoreCase(priorityName) ? Priority.TOP : "bottom".equalsIgnoreCase(priorityName) ? Priority.BOTTOM : null;
            final boolean defaultEnabled = JSONUtils.getBoolean(json, "defaultEnabled", true);

            if (priority == null) {
                throw new JsonParseException("Expected priority to be \"top\" or \"bottom\". " + priorityName + " is not a valid value!");
            }
            return new PackMeta(builtin, priority, defaultEnabled);
        }
    }

    public static class PackMeta {
        public final boolean isBuiltIn;
        public final Priority priority;
        public final boolean defaultEnabled;

        public PackMeta(boolean isBuiltIn, Priority priority, boolean defaultEnabled) {
            this.isBuiltIn = isBuiltIn;
            this.priority = priority;
            this.defaultEnabled = defaultEnabled;
        }

        @Override
        public String toString() {
            return "PackMeta [isBuiltIn=" + this.isBuiltIn + ", priority=" + this.priority + ", defaultEnabled=" + this.defaultEnabled + "]";
        }
    }
}