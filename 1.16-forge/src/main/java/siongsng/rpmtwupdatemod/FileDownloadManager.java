package siongsng.rpmtwupdatemod;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadManager implements FileDownloadManager_1 {
    private Thread downloadThread;
    private MainDownloader downloader;
    private DownloadStatus status = DownloadStatus.IDLE;

    public FileDownloadManager(String urlIn, String fileNameIn, String dirIn) {
        try {
            downloader = new MainDownloader(urlIn, fileNameIn, dirIn);
        } catch (IOException e) {
            catching(e);
        }
    }

    public void start(String threadName) {
        status = DownloadStatus.DOWNLOADING;
        downloadThread = new Thread(() -> {
            try {
                downloader.downloadResource();
            } catch (Throwable e) {
                catching(e);
            }
        }, threadName);
        downloadThread.start();
    }

    private void catching(Throwable e) {
        DownloadInfoHelper.info.add("資源包更新失敗。");
        RpmtwUpdateMod.LOGGER.warn("下載" + downloader.url + " fail");
        status = DownloadStatus.FAIL;
        downloader.done = true;
    }

    public DownloadStatus getStatus() {
        if ((status == DownloadStatus.DOWNLOADING || status == DownloadStatus.BACKGROUND) && downloader.done) {
            status = DownloadStatus.SUCCESS;
        }
        return status;
    }

    private static class MainDownloader {
        public float completePercentage = 0.0F;
        public boolean alive = true;
        Runnable successTask;
        private URL url;
        private String fileName;
        private String dirPlace;
        private int size = 0;
        private int downloadedSize = 0;
        private boolean done = false;

        MainDownloader(String urlIn, String fileName, String dirPlace) throws IOException {
            this.url = new URL(urlIn);
            this.fileName = fileName;
            this.dirPlace = dirPlace;
        }

        void downloadResource() throws Throwable {
            URLConnection connection = url.openConnection();

            connection.setConnectTimeout(10 * 1000);

            size = connection.getContentLength();

            InputStream inputStream = connection.getInputStream();
            byte[] getData = readInputStream(inputStream);

            if (getData != null) {
                File saveDir = new File(dirPlace);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                File file = new File(saveDir + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(file);

                fos.write(getData);
                fos.close();
                inputStream.close();
            }
            done = true;
            if (successTask != null) {
                successTask.run();
            }
        }

        private byte[] readInputStream(InputStream inputStream) throws IOException {
            byte[] buffer = new byte[64];
            int len;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                downloadedSize += len;
                completePercentage = (float) downloadedSize / (float) size;
                if (!alive) {
                    return null;
                }
            }
            bos.close();
            return bos.toByteArray();
        }
    }
}
