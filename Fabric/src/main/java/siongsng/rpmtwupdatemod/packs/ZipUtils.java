package siongsng.rpmtwupdatemod.packs;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

public class ZipUtils {
	public static void removeDirFromZip(File file,List<String> removeDirs) throws ZipException{

		List<String> removeList = new ArrayList<String>();
		ZipFile zipFile = null;
		try {
    		Long start = System.currentTimeMillis();
    		
    		zipFile = new ZipFile(file);
    		
    		zipFile.setCharset(Charset.forName("utf-8"));//字符集根據環境更換
    		
    		// 遍歷壓縮文件中所有的FileHeader

    		List<FileHeader> headersList = zipFile.getFileHeaders();

    		for (String dir : removeDirs) {
    			for (FileHeader subHeader : headersList) {
        			String subHeaderName = subHeader.getFileName();
        			//若 subHeaderName 字尾等於 條件則刪除
        			if (subHeaderName.endsWith(dir)) {
        				removeList.add(subHeaderName);
        				zipFile.getInputStream(subHeader).close();
        				continue;
        			}	
        		}
			}
    		
    		zipFile.removeFiles(removeList);

    		RpmtwUpdateMod.LOGGER.info("篩選耗時："+ (System.currentTimeMillis()-start)/1000.0+" 秒。");
    	}
    	catch(Exception ex) {
    		   RpmtwUpdateMod.LOGGER.error("發生未知錯誤: " , ex);
    	}finally {
            try {
            	if(zipFile != null) {
        			zipFile.close();
        		}
            } catch (Exception e) {
            }
        }
	}
}
