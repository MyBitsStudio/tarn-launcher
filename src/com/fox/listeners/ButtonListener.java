package com.fox.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import com.fox.Settings;
import com.tarn.Launcher;
import com.tarn.download.DownloadType;
import com.tarn.frame.AppFrame;
import com.fox.net.Download;
import com.fox.net.Update;
import com.tarn.io.UpdateChecker;
import com.tarn.utils.ClientUtils;
import com.tarn.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class ButtonListener implements ActionListener {
	
	public static Download download;
	
	@Override
	public void actionPerformed(@NotNull ActionEvent e) {
		switch (e.getActionCommand()) {

			case "updateClient" :
				if(Launcher.download.download(DownloadType.CLIENT)){
					new Thread(new UpdateChecker()).start();
				} else {
					AppFrame.clientUpdate.setText("Error");
					AppFrame.clientUpdate.setEnabled(false);
				}
				break;


			case "updateCache" :
				if(Launcher.download.download(DownloadType.CACHE)){
					new Thread(new UpdateChecker()).start();
				} else {
					AppFrame.cacheUpdate.setText("Error");
					AppFrame.cacheUpdate.setEnabled(false);
				}
				break;

			case "updateJava" :
				switch(Launcher.systemManager.getSystem()){
					case WINDOWS :
						if(Launcher.download.download(DownloadType.JAVA_WINDOWS)){
							new Thread(new UpdateChecker()).start();
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
					break;
					case MAC :
						if(Launcher.download.download(DownloadType.JAVA_MAC)){
							new Thread(new UpdateChecker()).start();
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
					break;
					case LINUX :
						if(Launcher.download.download(DownloadType.JAVA_LINUX)){
							new Thread(new UpdateChecker()).start();
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
					break;
					default :
						AppFrame.javaUpdate.setText("Error");
						AppFrame.javaUpdate.setEnabled(false);
					break;
				}
				break;

			case "_" : Launcher.app.setState(JFrame.ICONIFIED); break;
			case "X" : System.exit(0); break;
			case "play" : new Thread(() -> {

				AppFrame.playButton.setEnabled(false);
				AppFrame.pbar.setString("Please Wait . . .");
				AppFrame.pbar.setValue(20);

				Utils.pause(1000);

				AppFrame.pbar.setValue(40);
				AppFrame.pbar.setString("Checking Client . . .");

				if(!AppFrame.clientUpdated){
					AppFrame.pbar.setString("Client Needs Update. . .");
					AppFrame.pbar.setValue(0);
					AppFrame.playButton.setEnabled(true);
					return;
				}

				if(!AppFrame.cacheUpdated){
					AppFrame.pbar.setString("Cache Needs Update. . .");
					AppFrame.pbar.setValue(0);
					AppFrame.playButton.setEnabled(true);
					return;
				}

				Utils.pause(1000);

				AppFrame.pbar.setValue(60);
				AppFrame.pbar.setString("Client Found. Checking Java . . .");

				Utils.pause(1000);

				String version = Launcher.systemManager.getParam(0);

				if(version.startsWith("1.8")){
					Utils.pause(1000);

					AppFrame.pbar.setValue(80);
					AppFrame.pbar.setString("Java Found. Starting Tarn . . .");

					Utils.pause(1000);

					ClientUtils.startClient();
				} else if (Launcher.systemManager.version8()) {
					Utils.pause(1000);

					AppFrame.pbar.setValue(80);
					AppFrame.pbar.setString("Java Found. Starting Tarn . . .");

					Utils.pause(1000);

					ClientUtils.startClient();
				} else {
					switch (Launcher.systemManager.getSystem()) {
						case WINDOWS :
							if (Launcher.download.check(DownloadType.JAVA_WINDOWS) != 0) {
								AppFrame.pbar.setString("Java Needs Update. . .");
								AppFrame.pbar.setValue(0);
								AppFrame.playButton.setEnabled(true);
								return;
							}
						break;

						case MAC :
							if (Launcher.download.check(DownloadType.JAVA_MAC) != 0) {
								AppFrame.pbar.setString("Java Needs Update. . .");
								AppFrame.pbar.setValue(0);
								AppFrame.playButton.setEnabled(true);
								return;
							}
						break;

						case LINUX :
							if (Launcher.download.check(DownloadType.JAVA_LINUX) != 0) {
								AppFrame.pbar.setString("Java Needs Update. . .");
								AppFrame.pbar.setValue(0);
								AppFrame.playButton.setEnabled(true);
								return;
							}
						break;
					}
				}

				Utils.pause(1000);

				AppFrame.pbar.setValue(80);
				AppFrame.pbar.setString("Java Found. Starting Tarn . . .");

				Utils.pause(1000);

				ClientUtils.startClient();


			}).start();
			break;
			default : System.out.println(e.getActionCommand()); break;
		}
	}

}
