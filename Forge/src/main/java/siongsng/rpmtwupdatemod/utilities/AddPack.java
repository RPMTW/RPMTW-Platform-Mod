package siongsng.rpmtwupdatemod.utilities;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import org.apache.commons.io.FileUtils;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;


public class AddPack {
    public static Path PackDir = Minecraft.getMinecraft().getResourcePackRepository().getDirResourcepacks().toPath(); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.12.zip"); //資源包檔案位置

    public AddPack() {
        try {
            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), PackFile.toFile()); //下載資源包檔案
        } catch (IOException e) {
            e.printStackTrace();
        }
        setResourcesRepository();
    }

    void setResourcesRepository() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;

        if (!gameSettings.resourcePacks.contains("RPMTW-1.12.zip")) {
            mc.gameSettings.resourcePacks.add("RPMTW-1.12.zip");
        }
        reloadResources();
    }

    private void reloadResources() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;

        ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
        resourcePackRepository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> repositoryEntriesAll = resourcePackRepository.getRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();

        for (String packName : gameSettings.resourcePacks) {
            for (ResourcePackRepository.Entry entry : repositoryEntriesAll) {
                if (entry.getResourcePackName().equals(packName)) {
                    repositoryEntries.add(entry);
                    break;
                }
            }
        }

        resourcePackRepository.setRepositories(repositoryEntries);
    }
}
