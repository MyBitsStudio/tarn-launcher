package com.tarn.utils;

import com.tarn.Configuration;
import com.tarn.Launcher;
import com.tarn.download.DownloadType;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ClientUtils {

    public static void launch(String... params) throws IOException {
        try {
            Runtime.getRuntime().exec(params);
            Thread.sleep(150);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void startClient(){
        if(new File(DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName()).exists()){
            String version = Launcher.systemManager.getParam(0);
            String location = "";
            if(version.startsWith("1.8")){
               location = "";
            } else if (Launcher.systemManager.version8()) {
                if (!Launcher.systemManager.java864().equals("")) {
                    location = Launcher.systemManager.java864();
                } else if (!Launcher.systemManager.javaDownloaded().equals("")) {
                    location = Launcher.systemManager.javaDownloaded();
                } else if (Launcher.systemManager.java886().equals("")) {
                    location = "null";
                } else {
                    location = Launcher.systemManager.java886();
                }
            }

            if(location.equals("")){
                try {
                    launch("java", "-jar", DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    launch(location+"/bin/java.exe", "-jar", DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            System.out.println("Client not found!");
        }
    }

    public static void logEnv() {
        ProcessBuilder pb = new ProcessBuilder();

        Map<String, String> envMap = pb.environment();

        for (Map.Entry<String, String> entry :
                envMap.entrySet()) {
            // checking key and value separately
            System.out.println("Key = " + entry.getKey()
                    + ", Value = "
                    + entry.getValue());
        }
    }
}
