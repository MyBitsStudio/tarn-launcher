package com.tarn;

import com.fox.threads.ServerTime;
import com.tarn.core.SystemManager;
import com.tarn.frame.AppFrame;
import com.tarn.download.DownloadManager;
import com.tarn.download.DownloadType;
import com.tarn.frame.InfoPopup;
import com.tarn.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.util.stream.Stream;

public class Launcher {

    public static DownloadManager download;
    public static AppFrame app;

    public static SystemManager systemManager = new SystemManager();

    public static boolean isLocal = false;

    static {
        try {
            download = new DownloadManager();
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger logger = new Logger();

    public static void main(String[] main) {

        if(main.length > 0 && main[0].equals("local")){
            isLocal = true;
        }

        runChecks();

        if(download.download(DownloadType.LAUNCHER_ASSETS)){
            if(download.download(DownloadType.LAUNCHER)){
                new Thread(() -> {
                    loadSettings();

                    UIManager.put("Button.select", new Color(1.0f,1.0f, 1.0f, 0.05f));
                    System.setProperty("awt.useSystemAAFontSettings","on");
                    System.setProperty("swing.aatext", "true");

                    app = new AppFrame();
                    app.setVisible(true);
                    app.setLocationRelativeTo(null);
                }).start();
           }
        }

    }

    private static void runChecks(){
        if(!new File(Configuration.HOME).exists())
            new File(Configuration.HOME).mkdirs();

        if(!new File(Configuration.STORE_HOME).exists())
            new File(Configuration.STORE_HOME).mkdirs();

        if(!new File(Configuration.DATA_HOME).exists())
            new File(Configuration.DATA_HOME).mkdirs();

        if(!new File(Configuration.LOGS).exists())
            new File(Configuration.LOGS).mkdirs();

        if(!new File(Configuration.CACHE_HOME+"data/").exists())
            new File(Configuration.CACHE_HOME+"data/").mkdirs();

        if(!new File(Configuration.HOME+"settings.txt").exists()){
            writeSettings();
        }
    }

    public static void writeSettings(){
        try(FileWriter myWriter = new FileWriter(Configuration.HOME+"settings.txt")) {
            myWriter.write("auto-update :: "+Configuration.autoUpdate);
            myWriter.write(System.getProperty( "line.separator" ));
            myWriter.write("auto-check :: "+Configuration.autoCheck);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void loadSettings(){
        if(!new File(Configuration.HOME+"settings.txt").exists()){
            writeSettings();
        } else {
            try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(new File(Configuration.HOME+"settings.txt"))))) {
                stream.forEach(string -> {
                    String[] line = string.split(" :: ");
                    if(line[0].equals("auto-update")){
                        Configuration.autoUpdate = Boolean.parseBoolean(line[1]);
                    }
                    if(line[0].equals("auto-check")){
                        Configuration.autoCheck = Boolean.parseBoolean(line[1]);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
