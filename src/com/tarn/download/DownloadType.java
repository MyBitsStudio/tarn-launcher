package com.tarn.download;

import com.tarn.Configuration;
import com.tarn.core.OSystem;
import lombok.Getter;

@Getter
public enum DownloadType {

    LAUNCHER("Launcher", "launcher.tarn", "https://tarnserver.com/enforced/beta/downloads/Launcher.jar", "The launcher for Tarn", Configuration.CACHE_HOME, OSystem.ANY),
    CLIENT("Client", "client.tarn", "https://tarnserver.com/enforced/beta/downloads/TarnEX.jar", "The client for Tarn", Configuration.HOME, OSystem.ANY),
    CACHE("Cache", "cache.zip", "https://tarnserver.com/enforced/beta/cache/t-v1.zip", "The cache for Tarn", Configuration.DATA_HOME, OSystem.ANY),
    JAVA_MAC("Java For Mac", "java.zip", "https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u352-b08/openlogic-openjdk-8u352-b08-mac-x64.zip", "Java For Mac", Configuration.DATA_HOME, OSystem.MAC),
    JAVA_LINUX("Java For Linux", "java.tar.gz", "https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u352-b08/openlogic-openjdk-8u352-b08-linux-x64.tar.gz", "Java For Linux", Configuration.DATA_HOME, OSystem.LINUX),
    JAVA_WINDOWS("Java For Windows", "java.zip", "https://tarnserver.com/enforced/beta/java/java1.zip", "Java For Windows", Configuration.DATA_HOME, OSystem.WINDOWS),
    LAUNCHER_ASSETS("Launcher Assets", "data.zip", "https://tarnserver.com/enforced/beta/downloads/data.zip", "Data For Launcher", Configuration.CACHE_HOME+"data/", OSystem.ANY),

    ;


    private final String name, url, description, location, saveName;
    private final OSystem os;

    DownloadType(String name, String saveName, String url, String description, String location, OSystem os){
        this.name = name;
        this.saveName = saveName;
        this.url = url;
        this.description = description;
        this.location = location;
        this.os = os;
    }
}
