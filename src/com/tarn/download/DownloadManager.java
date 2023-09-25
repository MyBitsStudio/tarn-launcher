package com.tarn.download;

import com.tarn.Configuration;
import com.tarn.Launcher;
import com.tarn.frame.AppFrame;
import com.tarn.frame.InfoPopup;
import com.tarn.io.ThreadManager;
import com.tarn.logger.Log;
import com.tarn.logger.LogType;
import com.tarn.utils.ClientUtils;
import com.tarn.utils.crypto.Hashing;
import com.tarn.utils.crypto.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.SecretKey;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadManager {
    private final KeyStore store = KeyStore.getInstance("JCEKS");
    private final char[] pwdArray = Configuration.keys[1].replace("store-", "").toCharArray(),
    updateArray = Configuration.keys[2].replace("update-", "").toCharArray();

    private final InfoPopup popup;

    private DownloadType currentlyDownloading;

    public DownloadManager() throws KeyStoreException {
       loadStore();
       popup = new InfoPopup();
    }

    private void loadStore(){
        if(!new File(Configuration.STORE_HOME+"store.jks").exists()) {
            newStore();
            return;
        }
        try {
            store.load(new FileInputStream(Configuration.STORE_HOME+"store.jks"), pwdArray);
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void newStore(){
        try {
            store.load(null, pwdArray);
            SecretKey key = Keys.generate();
            KeyStore.SecretKeyEntry secret
                    = new KeyStore.SecretKeyEntry(key);
            KeyStore.ProtectionParameter password
                    = new KeyStore.PasswordProtection(updateArray);
            store.setEntry("update-encryption", secret, password);
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
        save();
    }

    public SecretKey getKey(String type, char[] key) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(key);
        return ((KeyStore.SecretKeyEntry)store.getEntry(type, protectionParam)).getSecretKey();
    }

    /**
     * Checks if the file is up to date
     * Status codes :
     * 0 = Up to date
     * 1 = Need update
     * 2 = Server Update File Error
     * @param type DownloadType
     * @return int
     */
    public int check(@NotNull DownloadType type){
        popup.sendPopup("Checking " + type.getName() + " version", Launcher.app);
        if(type.equals(DownloadType.LAUNCHER)){
            if(Launcher.isLocal){
                return 0;
            }
        }
        String hash = new File(type.getLocation() + type.getSaveName()).exists() ? Hashing.getLocalChecksum(type.getLocation() + type.getSaveName()) : "null";
        String remoteHash = Hashing.getRemoteChecksum(type.getUrl());
        if(remoteHash == null){
            Launcher.logger.log(
                    new Log("Remote server error", LogType.DOWNLOADER), false, true);
            popup.sendPopup("Remote Server Error", Launcher.app);
            return 2;
        } else if (hash == null || !hash.equals(remoteHash)) {
            Launcher.logger.log(
                    new Log("Needs Update", LogType.DOWNLOADER), false, true);
            popup.closePopup();
            return 1;
        } else {
            Launcher.logger.log(
                    new Log("Hashes Match", LogType.DOWNLOADER), false, true);
            popup.closePopup();
            return 0;
        }
    }

    /**
     * Downloads the file
     * @param type DownloadType
     */

    public boolean download(@NotNull DownloadType type) {
        Launcher.logger.log(
                new Log("Downloading " + type.getName(), LogType.DOWNLOADER), false, true);
        int status = check(type);
        if(currentlyDownloading != null){
            return false;
        }
        switch(status){
            case 1 :
                Launcher.logger.log(
                        new Log("Triggering " + type.getName(), LogType.DOWNLOADER), false, true);
                popup.sendPopup("Downloading " + type.getName()+"...", Launcher.app);
                triggerDownload(type);
                save();
                return true;
            case 2 : Launcher.logger.log(
                    new Log("Server Error " , LogType.DOWNLOADER), false, true);
                popup.sendPopup("Server Error", Launcher.app);
                break;
            default :
                if(!Launcher.isLocal){
                    if(type == DownloadType.LAUNCHER){
                        popup.closePopup();
                        updateLauncher(type);
                        return false;
                    }
                }
                Launcher.logger.log(
                        new Log("File up to date " + type.getName(), LogType.DOWNLOADER), false, true);
                popup.closePopup();
                return true;
        }
        return false;
    }



    private void updateLauncher(@NotNull DownloadType type){
        try {
            ClientUtils.launch("java", "-jar", type.getLocation() + File.separator + type.getSaveName(), "local");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void save(){
        if(!new File(Configuration.STORE_HOME).exists())
            new File(Configuration.STORE_HOME).mkdirs();

        try (FileOutputStream fos = new FileOutputStream(Configuration.STORE_HOME+"store.jks")) {
            store.store(fos, pwdArray);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void triggerDownload(DownloadType type){
        ThreadManager.execute(() -> {
            currentlyDownloading = type;
            try (InputStream inputStream = getInputStreamFromUrl(type.getUrl())) {
                if (inputStream == null) {
                    System.out.println("Failed to download " + type.getName());
                } else {
                    try (FileOutputStream outputStream = new FileOutputStream(type.getLocation() + File.separator + type.getSaveName())) {
                        downloadFile(inputStream, outputStream, type);
                        if (type.getUrl().contains("zip") || type.getUrl().contains("tar.gz")) {
                            unZipFile(type);
                        }
                    }
                    if(type == DownloadType.LAUNCHER){
                        updateLauncher(type);
                    }
                }

                popup.closePopup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void unZipFile(@NotNull DownloadType type){
        popup.sendPopup("Updating...", Launcher.app);
        try {
            byte[] buffer = new byte[296000];
            ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(type.getLocation() + File.separator + type.getSaveName())));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(new File(type.getLocation()), zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            currentlyDownloading = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        popup.closePopup();
    }

    public static @NotNull File newFile(File destinationDir, @NotNull ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private void downloadFile(@NotNull InputStream inputStream, FileOutputStream outputStream, DownloadType type) throws IOException {
        byte[] buffer = new byte[296000];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        currentlyDownloading = null;
        switch(type){
            case CLIENT : AppFrame.clBar.setString("Downloading 100%"); break;
            case CACHE : AppFrame.caBar.setString("Downloading 100%"); break;
            case JAVA_LINUX: case JAVA_MAC: case JAVA_WINDOWS : AppFrame.jaBar.setString("Downloading 100%"); break;
        }
    }

    private void updateDownloadStatus(long numWritten, int length, @NotNull DownloadType type, long currentTime) {
        int percentage = (int) (((double) numWritten / (double) length) * 100D);
        int downloadSpeed = (int) ((numWritten / 1024) / (1 + (currentTime / 1000)));

        switch(type){
            case CLIENT :
                AppFrame.clBar.setValue(percentage);
                AppFrame.clBar.setString("Downloading " + type.getName() + " (" + downloadSpeed + "kb/s): " + percentage + "%");
            break;
            case CACHE :
                AppFrame.caBar.setValue(percentage);
                AppFrame.caBar.setString("Downloading " + type.getName() + " (" + downloadSpeed + "kb/s): " + percentage + "%");
            break;
            case JAVA_LINUX: case JAVA_MAC: case JAVA_WINDOWS :
                AppFrame.jaBar.setValue(percentage);
                AppFrame.jaBar.setString("Downloading " + type.getName() + " (" + downloadSpeed + "kb/s): " + percentage + "%");
            break;
        }
    }

    private @Nullable InputStream getInputStreamFromUrl(String downloadUrl) throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.addRequestProperty("User-Agent", "Mozilla/4.76");
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            System.out.println(downloadUrl + " returned code " + responseCode);
            httpConn.disconnect();
            return null;
        }
    }

}
