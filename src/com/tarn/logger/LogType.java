package com.tarn.logger;

public enum LogType {

    DOWNLOADER("download", "", "\u001B[32m", "[DOWNLOADER]")

    ;

    private final String name, location, prefix, pre;

    LogType(String name, String location, String prefix, String pre){
        this.name = name;
        this.location = location;
        this.prefix = prefix;
        this.pre = pre;
    }

    public String getName(){
        return name;
    }

    public String getLocation(){
        return location;
    }

    public String getPrefix(){
        return prefix;
    }

    public String getPre(){
        return pre;
    }

}
