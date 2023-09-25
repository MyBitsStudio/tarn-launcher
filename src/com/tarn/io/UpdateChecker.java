package com.tarn.io;

import com.tarn.Launcher;
import com.tarn.download.DownloadType;
import com.tarn.frame.AppFrame;

import java.awt.*;
import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateChecker implements Runnable {

    private final boolean[] checks = new boolean[3];
    private final AtomicBoolean updateJava = new AtomicBoolean(false),
            updateClient = new AtomicBoolean(false), updateCache = new AtomicBoolean(false);


    @Override
    public void run() {
        checkJava();
        checkClient();
        checkCache();

        while(updateJava.get() || updateClient.get() || updateCache.get()){
            try{
                Thread.sleep(100);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        if(checks[1] && checks[2]){
            AppFrame.playButton.setEnabled(true);
            AppFrame.playButton.setBackground(Color.GREEN);
            AppFrame.pbar.setString("Ready To Play! Click Launch Now!");
        } else {
            AppFrame.playButton.setEnabled(false);
            AppFrame.pbar.setString("Updates Needed To Launch. . .");
        }
    }

    private void checkCache(){
        updateCache.set(true);
        AppFrame.caBar.setString("Checking Cache . . .");
        File file = new File(DownloadType.CACHE.getLocation() + DownloadType.CACHE.getSaveName());
        if(file.exists()){
            AppFrame.cache.setText("Cache Location : " + file.getAbsolutePath());
            switch(Launcher.download.check(DownloadType.CACHE)){
                case 0 :
                    AppFrame.cacheVersion.setText("Cache Version : Up To Date");
                    AppFrame.caBar.setValue(100);
                    AppFrame.caBar.setString("Cache Up To Date");
                    AppFrame.cacheUpdate.setText("Ready!");
                    AppFrame.cacheUpdate.setBackground(Color.GREEN);
                    AppFrame.cacheUpdate.setEnabled(false);
                    AppFrame.cacheUpdated = true;
                    checks[0] = true;
                break;
                case 1 :
                    AppFrame.cacheVersion.setText("Cache Version : Outdated");
                    AppFrame.caBar.setValue(50);
                    AppFrame.caBar.setString("Cache Out Of Date");
                    AppFrame.cacheUpdate.setText("Update");
                    AppFrame.cacheUpdate.setBackground(Color.YELLOW);
                    AppFrame.cacheUpdate.setEnabled(true);
                    if(Launcher.download.download(DownloadType.CACHE)){
                        Future<?> future = ThreadManager.executor.submit(new UpdateChecker());
                        try {
                            future.get(300, TimeUnit.SECONDS);
                        } catch (TimeoutException te) {
                            future.cancel(true);
                            Launcher.download.sendPopup("Timed out. Please check again");
                            AppFrame.cacheUpdate.setText("Failed - Press Me");
                            AppFrame.cacheUpdate.setEnabled(true);
                        } catch (Exception ex) {
                            Launcher.download.sendPopup("Cache update checker error");
                        }
                    }
                break;
                default :
                    AppFrame.cacheVersion.setText("Cache Version : Server Error");
                    AppFrame.caBar.setValue(25);
                    AppFrame.caBar.setString("Server Error");
                    AppFrame.cacheUpdate.setText("Error");
                    AppFrame.cacheUpdate.setBackground(Color.RED);
                    AppFrame.cacheUpdate.setEnabled(false);
                break;
            }
        } else {
            AppFrame.cache.setText("Cache Location : Not Found");
            AppFrame.caBar.setValue(0);
            AppFrame.caBar.setString("Cache Not Found");
            AppFrame.cacheVersion.setText("Cache Version : Not Found");
            AppFrame.cacheUpdate.setBackground(Color.BLUE);
            AppFrame.cacheUpdate.setText("Update");
            AppFrame.cacheUpdate.setEnabled(true);
            if(Launcher.download.download(DownloadType.CACHE)){
                Future<?> future = ThreadManager.executor.submit(new UpdateChecker());
                try {
                    future.get(300, TimeUnit.SECONDS);
                } catch (TimeoutException te) {
                    future.cancel(true);
                    Launcher.download.sendPopup("Timed out. Please check again");
                    AppFrame.cacheUpdate.setText("Failed - Press Me");
                    AppFrame.cacheUpdate.setEnabled(true);
                } catch (Exception ex) {
                    Launcher.download.sendPopup("Error checking cache");
                }
            }
        }
        updateCache.set(false);
    }

    private void checkClient(){
        updateClient.set(true);
        AppFrame.clBar.setString("Checking Client . . .");
        File file = new File(DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName());
        if(file.exists()){
            AppFrame.clients.setText("Client Location : " + file.getAbsolutePath());
            switch(Launcher.download.check(DownloadType.CLIENT)){
                case 0 :
                    AppFrame.version.setText("Client Version : Up To Date");
                    AppFrame.clBar.setValue(100);
                    AppFrame.clBar.setString("Client Up To Date");
                    AppFrame.clientUpdate.setText("Ready!");
                    AppFrame.clientUpdate.setBackground(Color.GREEN);
                    AppFrame.clientUpdate.setEnabled(false);
                    AppFrame.clientUpdated = true;
                    checks[1] = true;
                break;
                case 1 :
                    AppFrame.version.setText("Client Version : Outdated");
                    AppFrame.clBar.setValue(50);
                    AppFrame.clBar.setString("Client Out Of Date");
                    AppFrame.clientUpdate.setBackground(Color.YELLOW);
                    AppFrame.clientUpdate.setText("Update");
                    AppFrame.clientUpdate.setEnabled(true);
                    if(Launcher.download.download(DownloadType.CLIENT)){
                        Future<?> future = ThreadManager.executor.submit(new UpdateChecker());
                        try {
                            future.get(30, TimeUnit.SECONDS);
                        } catch (TimeoutException te) {
                            future.cancel(true);
                            Launcher.download.sendPopup("Timed out. Please check again");
                            AppFrame.clientUpdate.setText("Failed - Press me");
                            AppFrame.clientUpdate.setEnabled(true);
                        } catch (Exception ex) {
                            Launcher.download.sendPopup("Error checking client");
                            // handle other exceptions
                        }
                    }
                break;
                default :
                    AppFrame.version.setText("Client Version : Server Error");
                    AppFrame.clBar.setValue(25);
                    AppFrame.clBar.setString("Server Error");
                    AppFrame.clientUpdate.setBackground(Color.RED);
                    AppFrame.clientUpdate.setText("Error");
                    AppFrame.clientUpdate.setEnabled(false);
                break;
            }

        } else {
            AppFrame.clients.setText("Client Location : Not Found");
            AppFrame.clBar.setValue(0);
            AppFrame.clBar.setString("Client Not Found");
            AppFrame.version.setText("Client Version : Not Found");
            AppFrame.clientUpdate.setBackground(Color.BLUE);
            AppFrame.clientUpdate.setText("Update");
            AppFrame.clientUpdate.setEnabled(true);
            if(Launcher.download.download(DownloadType.CLIENT)){
                Future<?> future = ThreadManager.executor.submit(new UpdateChecker());
                try {
                    future.get(30, TimeUnit.SECONDS);
                } catch (TimeoutException te) {
                    future.cancel(true);
                    Launcher.download.sendPopup("Timed out. Please check again");
                    AppFrame.clientUpdate.setText("Failed - Press Me");
                    AppFrame.clientUpdate.setEnabled(true);
                } catch (Exception ex) {
                    Launcher.download.sendPopup("Error checking client");
                    // handle other exceptions
                }
            }
        }
        updateClient.set(false);
    }

    private void checkJava(){
        updateJava.set(true);
        AppFrame.jaBar.setString("Checking Java . . .");
        String version = Launcher.systemManager.getParam(0);
        AppFrame.javaVersion.setText("Java Version : " + version);
        AppFrame.jaBar.setValue(20);
        if(version.startsWith("1.8")){
            AppFrame.java.setText("Java Location : " + Launcher.systemManager.java864());
            AppFrame.jaBar.setString("Java 8 is installed!");
            AppFrame.jaBar.setValue(100);
            AppFrame.javaUpdate.setText("Ready!");
            AppFrame.javaUpdate.setBackground(Color.GREEN);
            AppFrame.javaUpdate.setEnabled(false);
            AppFrame.javaUpdated = true;
            checks[2] = true;
        } else {
            AppFrame.jaBar.setString("Checking . . .");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(Launcher.systemManager.version8()){
                if(!Launcher.systemManager.java864().equals("")){
                    AppFrame.java.setText("Java Location : " + Launcher.systemManager.java864());
                    AppFrame.jaBar.setString("Java 8 is installed!");
                    AppFrame.jaBar.setValue(100);
                    AppFrame.javaUpdate.setText("Ready!");
                    AppFrame.javaUpdate.setBackground(Color.GREEN);
                    AppFrame.javaUpdate.setEnabled(false);
                    AppFrame.javaUpdated = true;
                    checks[2] = true;
                } else if(!Launcher.systemManager.javaDownloaded().equals("")){
                    AppFrame.java.setText("Java Location : " + Launcher.systemManager.javaDownloaded());
                    AppFrame.jaBar.setString("Java 8 is installed!");
                    AppFrame.jaBar.setValue(100);
                    AppFrame.javaUpdate.setText("Ready!");
                    AppFrame.javaUpdate.setBackground(Color.GREEN);
                    AppFrame.javaUpdate.setEnabled(false);
                    AppFrame.javaUpdated = true;
                    checks[2] = true;
                } else if(Launcher.systemManager.java886().equals("")) {
                    AppFrame.jaBar.setString("Not Installed...");
                    AppFrame.jaBar.setValue(0);
                    AppFrame.javaUpdate.setText("Update");
                    AppFrame.javaUpdate.setBackground(Color.YELLOW);
                    AppFrame.javaUpdate.setEnabled(true);
                } else {
                    AppFrame.java.setText("Java Location : " + Launcher.systemManager.java886());
                    AppFrame.jaBar.setString("Java 8 Installed.");
                    AppFrame.jaBar.setValue(100);
                    AppFrame.javaUpdate.setText("Ready!");
                    AppFrame.javaUpdate.setBackground(Color.GREEN);
                    AppFrame.javaUpdate.setEnabled(false);
                    AppFrame.javaUpdated = true;
                    checks[2] = true;
                }
            } else {
                AppFrame.jaBar.setString("Not Installed. Please download.");
                AppFrame.jaBar.setValue(0);
                AppFrame.javaUpdate.setText("Update");
                AppFrame.javaUpdate.setBackground(Color.BLUE);
                AppFrame.javaUpdate.setEnabled(true);
            }
        }
        updateJava.set(false);

    }


}
