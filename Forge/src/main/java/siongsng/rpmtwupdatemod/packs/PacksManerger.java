package siongsng.rpmtwupdatemod.packs;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.crowdin.RPMKeyBinding;
import siongsng.rpmtwupdatemod.function.SendMsg;

public  class PacksManerger {
	public static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.16"); //資源包的放置根目錄
    public static Path PackFile = PackDir.resolve("RPMTW-1.16.zip"); //資源包檔案位置
    public static String UpdateFile = PackDir + "/Update.txt"; //更新暫存檔案放置位置
    public static String Latest_ver = json.ver("https://api.github.com/repos/RPMTW/ResourcePack-Mod-zh_tw/releases/latest").toString(); //取得最新版本Tag
    public static String Latest_ver_n = Latest_ver.split("RPMTW-1.16-V")[1]; //取得數字ID
    private static PackFinder RESOUCE;
    
    public static void close() {
    	if(RESOUCE != null)
    		RESOUCE.close();
    }
    
	 public static void ReloadPack() {
	        SendMsg.send("由於你按下了檢測翻譯包更新快捷鍵，因此開始進行翻譯包更新檢測作業，請稍後...");
	        Thread thread = new Thread(() -> {
	            try {
	                if (Files.exists(PackFile)) {
	                	File file = PackFile.toFile();
	                	close();
	                	RESOUCE = new PackFinder();
	                    long fileTime = Files.getLastModifiedTime(PackFile).toMillis();
	                    long nowTime = System.currentTimeMillis();
	                    if (TimeUnit.MILLISECONDS.toMinutes(nowTime - fileTime) < 1) {
	                        SendMsg.send("§6偵測到翻譯包版本過舊，正在進行更新並重新載入中...。");
	                    }
	                    FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl), file); //下載資源包檔案
	                    ZipUtils.removeDirFromZip(file, Configer.modBlackList.get());
	                    Minecraft.getInstance().getResourcePackList().addPackFinder(RESOUCE); //新增資源包至資源包列表
	                    Minecraft.getInstance().reloadResources().whenCompleteAsync((c,t)->{//重新載入資源
	                    	 RPMKeyBinding.updateLock = false;
	                    	 SendMsg.send("§b處理完成。");
	                    });

	                } else {
	                    SendMsg.send("§a目前的RPMTW翻譯包版本已經是最新的了!因此不進行更新作業。");
	                }
	            } catch (Exception e) {
	                RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
	            }
	        });
	        thread.start();
	    }
	 
	    public static void  PackVersionCheck() {
	        try {
	        	if(RESOUCE == null){
	        		File file = PackFile.toFile();
	        		RESOUCE = new PackFinder();
		            RpmtwUpdateMod.LOGGER.info("正在準備檢測資源包版本，最新版本:" + Latest_ver);
		            if (!Files.isDirectory(PackDir)) { //如果沒有資源包路徑
		                Files.createDirectories(PackDir);
		            }
		            if (!Files.exists(Paths.get(PackDir + "/Update.txt"))) { //如果沒有更新檔案
		                Files.createFile(Paths.get(PackDir + "/Update.txt")); //建立更新檔案
		                FileWriter.Writer(Latest_ver_n, UpdateFile); //寫入最新版本
		            }
		            FileWriter.Writer(Latest_ver_n, UpdateFile); //寫入最新版本
		            FileUtils.copyURLToFile(new URL(RpmtwUpdateMod.PackDownloadUrl),file ); //下載資源包檔案
		        	ZipUtils.removeDirFromZip(file, Configer.modBlackList.get());
		        	Minecraft.getInstance().getResourcePackList().addPackFinder(RESOUCE); //新增資源包至資源包列表
	        	}
	        } catch (Exception e) {
	            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " + e); //錯誤處理
	        }
	    }
}
