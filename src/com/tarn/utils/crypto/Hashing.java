package com.tarn.utils.crypto;

import com.tarn.Launcher;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static String getLocalChecksum(String location) {
        File local = new File(location);
        try (FileInputStream fis = new FileInputStream(local)) {
            return calculateMd5(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRemoteChecksum(String location) {
        try (InputStream stream = getInputStreamFromUrl(location)) {
            return calculateMd5(stream);
        } catch (Exception e) {
            Launcher.download.sendPopup("Error checking server checksum.");
            return null;
        }
    }

    private static @Nullable InputStream getInputStreamFromUrl(String downloadUrl) throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.addRequestProperty("User-Agent", "Mozilla/4.76");
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            Launcher.download.sendPopup("Server error, can't reach site.");
            System.out.println(downloadUrl + " returned code " + responseCode);
            httpConn.disconnect();
            return null;
        }
    }

    public static String calculateMd5(final InputStream instream) {
        return calculateDigest(instream);
    }

    private static String calculateDigest(final InputStream instream) {
        final byte[] buffer = new byte[4096];
        final MessageDigest messageDigest = getMessageDigest("SHA-256");
        messageDigest.reset();
        int bytesRead;
        try {
            while ((bytesRead = instream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Error making a '" + "SHA-256" + "' digest on the inputstream");
        }
        return toHex(messageDigest.digest());
    }

    public static String toHex(final byte[] ba) {
        int baLen = ba.length;
        char[] hexchars = new char[baLen * 2];
        int cIdx = 0;
        for (byte b : ba) {
            hexchars[cIdx++] = hexdigit[(b >> 4) & 0x0F];
            hexchars[cIdx++] = hexdigit[b & 0x0F];
        }
        return String.valueOf(hexchars);
    }

    public static MessageDigest getMessageDigest(final String algorithm) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("The '" + algorithm + "' algorithm is not available");
        }
        return messageDigest;
    }

    private static final char[] hexdigit = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

}
