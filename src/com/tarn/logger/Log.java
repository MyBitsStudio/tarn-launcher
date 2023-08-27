package com.tarn.logger;

import com.tarn.utils.Utils;

public class Log {

    private final String message;
    private final LogType type;

    private final long time;

    public Log(String message, LogType type){
        this.message = message;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

    public String getMessage(){
        return message;
    }

    public String formattedMessage(){
        return type.getPrefix() + type.getPre() + " "+ Utils.formatMillisToDate(time)+" - " + message;
    }


}
