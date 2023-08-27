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
import java.security.KeyStoreException;

public class Launcher {

    public static DownloadManager download;
    public static AppFrame app;

    public static SystemManager systemManager = new SystemManager();

    public static boolean isLocal = false;

    public static InfoPopup infoPopup = new InfoPopup();

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

       if(download.download(DownloadType.LAUNCHER)){
            if(download.download(DownloadType.LAUNCHER_ASSETS)){
                UIManager.put("Button.select", new Color(1.0f,1.0f, 1.0f, 0.05f));
                System.setProperty("awt.useSystemAAFontSettings","on");
                System.setProperty("swing.aatext", "true");

                app = new AppFrame();
                app.setVisible(true);
                app.setLocationRelativeTo(null);
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
    }
}
