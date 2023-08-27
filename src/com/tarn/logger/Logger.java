package com.tarn.logger;

public class Logger {

    public Logger(){

    }

    public void log(Log log, boolean save, boolean show){
        if(show)
            System.out.println(log.formattedMessage()+"\u001B[0m");
        if(save){

        }
    }
}
