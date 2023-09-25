package com.tarn;

import java.awt.*;

public class Configuration {

    public static String CACHE_HOME = System.getProperty("user.home") + "/.tarn/launcher/";
    public static String STORE_HOME = System.getProperty("user.home") + "/.tarn/store/";
    public static String DATA_HOME = System.getProperty("user.home") + "/.tarn/data/";
    public static String HOME = System.getProperty("user.home") + "/.tarn/";
    public static String LOGS = System.getProperty("user.home") + "/.tarn/logs/";


    public static String[] keys = new String[]{
            "checkfile-Rq5TfSE6XS92RA9EWdnZ5",
            "store-EFyVlF0ThJE5s7L4",
            "key-nq0leUFO6XS3SS8VCjBiM1Tt"
    };

    public static String SERVER_IP = "tarn-server-raid-01-enforced-live.tarnserver.live";
    public static int SERVER_PORT = 42166;

    public static String SERVER_NAME = "Tarn";

    public static final Dimension frameSize = new Dimension(1060, 620);
    public static final Color helpColor = new Color(203, 11, 11);
    public static final Color backgroundColor = new Color(30, 30, 30);
    public static final Color primaryColor = new Color(32, 119, 7);
    public static final Color iconShadow = new Color(0, 0, 0);
    public static final Color buttonDefaultColor = new Color(255, 255, 255);

    public static String VOTE_SITE = "https://tarnserver.com/voting", STORE_SITE = "https://tarnserver.com/donate", DISCORD_LINK = "https://discord.gg/c5KaC3pBsn";
}
