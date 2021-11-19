package siongsng.rpmtwupdatemod.packs;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ZipUtils {
    final static Path PackDir = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.17");
    final static Path PackFile = PackDir.resolve("RPMTW-1.17.zip");
    private static int retryCount = 0;

    public static void removeDirFromZip(File file, List<String> removeDirs) throws ZipException {
        PackManeger.close();
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
            PackManeger.close();
            zipFile.removeFiles(removeList);

            RpmtwUpdateMod.LOGGER.info("篩選耗時：" + (System.currentTimeMillis() - start) / 1000.0 + " 秒。");
            if (retryCount > 0) {
                RpmtwUpdateMod.LOGGER.info("重新嘗試成功,已刪除暫存檔案");
                File[] files = Paths.get(System.getProperty("user.home") + "/.rpmtw/1.17").toFile().listFiles((d, s) -> {
                    return s.toLowerCase().contains(".zip") && !s.toLowerCase().endsWith(".zip");
                });
                for (File f : files) {
                    if (f.isFile())
                        f.delete();
                }
                retryCount = 0;
            }

        } catch (ZipException zipEx) {
            if (retryCount < 5) {
                retryCount++;
                RpmtwUpdateMod.LOGGER.warn(zipEx.getMessage() + " 重新嘗試 (" + retryCount + ")");
                removeDirFromZip(file, removeDirs);
            } else {
                RpmtwUpdateMod.LOGGER.error(zipEx.getMessage() + " 已超過重試次數");
                retryCount = 0;
            }

        } catch (Exception ex) {
            RpmtwUpdateMod.LOGGER.error("發生未知錯誤: ", ex);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
