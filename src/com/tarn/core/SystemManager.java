package com.tarn.core;

import com.tarn.download.DownloadType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class SystemManager {

    private final OSystem system = setOperatingSystem();
    /**
     * System Parameters
     * 0 -- Java Version
     * 1 -- Java 8 Location
     */
    private final String[] systemParams = new String[16];

    public SystemManager(){
        systemParams[0] = System.getProperty("java.version");
    }

    public String getParam(int index){
        return systemParams[index];
    }

    public void setSystemParams(int index, String value){
        systemParams[index] = value;
    }

    public OSystem getSystem() {
        return system;
    }

    public OSystem setOperatingSystem() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            return OSystem.MAC;
        } else if (OS.contains("win")) {
            return OSystem.WINDOWS;
        } else if (OS.contains("nux")) {
            return OSystem.LINUX;
        } else {
            return OSystem.ANY;
        }
    }

    public boolean version8(){
        return !java864().equals("") || !java886().equals("") || !javaDownloaded().equals("");
    }

    @NotNull
    public String java864(){
        File file = new File(fileDir()+":\\Program Files\\Java\\");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : Objects.requireNonNull(files)){
                if(f.getName().contains("1.8.0_")){
                    return f.getAbsolutePath();
                }
            }
        }
        return "";
    }

    @NotNull
    public String javaDownloaded(){
        File file;
        switch(getSystem()){
            case WINDOWS :
                file = new File(DownloadType.JAVA_WINDOWS.getLocation() + DownloadType.JAVA_WINDOWS.getSaveName());
                if(file.exists()){
                    return file.getAbsolutePath();
                }
            break;
            case MAC :
                file = new File(DownloadType.JAVA_MAC.getLocation() + DownloadType.JAVA_MAC.getSaveName());
                if(file.exists()){
                    return file.getAbsolutePath();
                }
            break;
            case LINUX:
                file = new File(DownloadType.JAVA_LINUX.getLocation() + DownloadType.JAVA_LINUX.getSaveName());
                if(file.exists()){
                    return file.getAbsolutePath();
                }
            break;
        }
        return "";
    }

    private @NotNull String fileDir(){
        return String.valueOf(System.getProperty("java.home").charAt(0));
    }

    @NotNull
    public String java886(){
        File file = new File(fileDir()+":\\Program Files (x86)\\Java\\");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : Objects.requireNonNull(files)){
                if(f.getName().contains("1.8.0_")){
                    return f.getAbsolutePath();
                }
            }
        }
        return "";
    }


}
