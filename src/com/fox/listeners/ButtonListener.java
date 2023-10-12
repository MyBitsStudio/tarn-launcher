package com.fox.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;

import com.tarn.Configuration;
import com.tarn.Launcher;
import com.tarn.download.DownloadManager;
import com.tarn.download.DownloadType;
import com.tarn.frame.AppFrame;
import com.fox.net.Download;
import com.tarn.frame.SettingPopup;
import com.tarn.io.ThreadManager;
import com.tarn.io.UpdateChecker;
import com.tarn.utils.ClientUtils;
import com.tarn.utils.Utils;
import org.jetbrains.annotations.NotNull;

import static com.tarn.download.DownloadManager.newFile;

public class ButtonListener implements ActionListener {


	@Override
	public void actionPerformed(@NotNull ActionEvent e) {
		if(e.getActionCommand().contains("Auto-Update")){
			Configuration.autoUpdate = !Configuration.autoUpdate;
			Launcher.writeSettings();
			AppFrame.settings.closePopup();
			AppFrame.settings.sendPopup(null);
		}
		if(e.getActionCommand().contains("Auto-Check")){
			Configuration.autoCheck = !Configuration.autoCheck;
			Launcher.writeSettings();
			AppFrame.settings.closePopup();
			AppFrame.settings.sendPopup(null);
		}
		switch (e.getActionCommand()) {
			case "?":
				AppFrame.settings = new SettingPopup();
				AppFrame.settings.sendPopup(Launcher.app);
				break;
			case "forceClient":
				ClientUtils.startClient();
				break;
			case "deleteClient":
				if (new File(DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName()).exists()) {
					if (new File(DownloadType.CLIENT.getLocation() + DownloadType.CLIENT.getSaveName()).delete()) {
						Launcher.download.sendPopup("Client has been deleted. Please run check.");
					} else {
						Launcher.download.sendPopup("Something went wrong here...");
					}
				} else {
					Launcher.download.sendPopup("Client doesn't exist. Please run check.");
				}
				break;
			case "deleteCache":
				if (new File(Configuration.DATA_HOME).exists()) {
					if (new File(Configuration.DATA_HOME).delete()) {
						Launcher.download.sendPopup("Client has been deleted. Please run check.");
					} else {
						Launcher.download.sendPopup("Something went wrong here...");
					}
				} else {
					Launcher.download.sendPopup("Client doesn't exist. Please run check.");
				}
				break;
			case "forceCheck" :
				ThreadManager.execute(() -> {
					Future<?> future = ThreadManager.executor.submit(new UpdateChecker());
					try {
						future.get(300, TimeUnit.SECONDS);
					} catch (TimeoutException te) {
						future.cancel(true);
						Launcher.download.sendPopup("Check Timeout. Please try again.");
					} catch (Exception ex) {
						Launcher.download.sendPopup("Check failed. Please try again.");
					}
				});
				break;
			case "forceCache":
				Launcher.download.sendPopup("Forcing download... Please wait...");
				ThreadManager.execute(() -> {
					DownloadType type = DownloadType.CACHE;
					try (InputStream inputStream = DownloadManager.getInputStreamFromUrl(type.getUrl())) {
						if (inputStream == null) {
							System.out.println("Failed to download " + type.getName());
						} else {
							try (FileOutputStream outputStream = new FileOutputStream(type.getLocation() + File.separator + type.getSaveName())) {
								downloadFile(inputStream, outputStream, type);
								if (type.getUrl().contains("zip") || type.getUrl().contains("tar.gz")) {
									unZipFile(type);
								}
							}
						}
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				});
				break;
			case "fClient":
				Launcher.download.sendPopup("Forcing download... Please wait...");
				ThreadManager.execute(() -> {
					DownloadType type = DownloadType.CLIENT;
					try (InputStream inputStream = DownloadManager.getInputStreamFromUrl(type.getUrl())) {
						if (inputStream == null) {
							System.out.println("Failed to download " + type.getName());
						} else {
							try (FileOutputStream outputStream = new FileOutputStream(type.getLocation() + File.separator + type.getSaveName())) {
								downloadFile(inputStream, outputStream, type);
							}

						}
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					Launcher.download.sendPopup("Download Complete");
				});
				break;
			case "updateClient":
				if (Launcher.download.download(DownloadType.CLIENT)) {
					Future<?> futures = ThreadManager.executor.submit(new UpdateChecker());
					try {
						futures.get(300, TimeUnit.SECONDS);
					} catch (TimeoutException te) {
						futures.cancel(true);
						AppFrame.clientUpdate.setText("Failed - Press Me");
						AppFrame.clientUpdate.setEnabled(true);
					} catch (Exception ex) {
						// handle other exceptions
					}
				} else {
					AppFrame.clientUpdate.setText("Error");
					AppFrame.clientUpdate.setEnabled(false);
				}
				break;


			case "updateCache":
				if (Launcher.download.download(DownloadType.CACHE)) {
					Future<?> future1 = ThreadManager.executor.submit(new UpdateChecker());
					try {
						future1.get(300, TimeUnit.SECONDS);
					} catch (TimeoutException te) {
						future1.cancel(true);
						AppFrame.cacheUpdate.setText("Failed- Press Me");
						AppFrame.cacheUpdate.setEnabled(true);
					} catch (Exception ex) {
						// handle other exceptions
					}
				} else {
					AppFrame.cacheUpdate.setText("Error");
					AppFrame.cacheUpdate.setEnabled(false);
				}
				break;

			case "updateJava":
				switch (Launcher.systemManager.getSystem()) {
					case WINDOWS:
						if (Launcher.download.download(DownloadType.JAVA_WINDOWS)) {
							Future<?> future2 = ThreadManager.executor.submit(new UpdateChecker());
							try {
								future2.get(300, TimeUnit.SECONDS);
							} catch (TimeoutException te) {
								future2.cancel(true);
								AppFrame.javaUpdate.setText("Failed- Press Me");
								AppFrame.javaUpdate.setEnabled(true);
							} catch (Exception ex) {
								// handle other exceptions
							}
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
						break;
					case MAC:
						if (Launcher.download.download(DownloadType.JAVA_MAC)) {
							Future<?> future3 = ThreadManager.executor.submit(new UpdateChecker());
							try {
								future3.get(300, TimeUnit.SECONDS);
							} catch (TimeoutException te) {
								future3.cancel(true);
								AppFrame.javaUpdate.setText("Failed- Press Me");
								AppFrame.javaUpdate.setEnabled(true);
							} catch (Exception ex) {
								// handle other exceptions
							}
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
						break;
					case LINUX:
						if (Launcher.download.download(DownloadType.JAVA_LINUX)) {
							Future<?> future4 = ThreadManager.executor.submit(new UpdateChecker());
							try {
								future4.get(300, TimeUnit.SECONDS);
							} catch (TimeoutException te) {
								future4.cancel(true);
								AppFrame.javaUpdate.setText("Failed- Press Me");
								AppFrame.javaUpdate.setEnabled(true);
							} catch (Exception ex) {
								// handle other exceptions
							}
						} else {
							AppFrame.javaUpdate.setText("Error");
							AppFrame.javaUpdate.setEnabled(false);
						}
						break;
					default:
						AppFrame.javaUpdate.setText("Error");
						AppFrame.javaUpdate.setEnabled(false);
						break;
				}
				break;

			case "_":
				Launcher.app.setState(JFrame.ICONIFIED);
				break;
			case "X":
				System.exit(0);
				break;
			case "play":
				ThreadManager.execute(() -> {
					AppFrame.playButton.setEnabled(false);
					AppFrame.pbar.setString("Please Wait . . .");
					AppFrame.pbar.setValue(20);

					Utils.pause(1000);

					AppFrame.pbar.setValue(40);
					AppFrame.pbar.setString("Checking Client . . .");

					if (!AppFrame.clientUpdated) {
						AppFrame.pbar.setString("Client Needs Update. . .");
						AppFrame.pbar.setValue(0);
						AppFrame.playButton.setEnabled(true);
						return;
					}

					if (!AppFrame.cacheUpdated) {
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

					if (version.startsWith("1.8")) {
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
							case WINDOWS:
								if (Launcher.download.check(DownloadType.JAVA_WINDOWS) != 0) {
									AppFrame.pbar.setString("Java Needs Update. . .");
									AppFrame.pbar.setValue(0);
									AppFrame.playButton.setEnabled(true);
									return;
								}
								break;

							case MAC:
								if (Launcher.download.check(DownloadType.JAVA_MAC) != 0) {
									AppFrame.pbar.setString("Java Needs Update. . .");
									AppFrame.pbar.setValue(0);
									AppFrame.playButton.setEnabled(true);
									return;
								}
								break;

							case LINUX:
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


				});
				break;
			default:
				System.out.println(e.getActionCommand());
				break;
		}
	}

	private void downloadFile(@NotNull InputStream inputStream, FileOutputStream outputStream, DownloadType type) throws IOException {
		byte[] buffer = new byte[296000];
		int bytesRead;

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
	}

	private void unZipFile(@NotNull DownloadType type){
		try {
			byte[] buffer = new byte[296000];
			ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(type.getLocation() + File.separator + type.getSaveName())));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(new File(type.getLocation()), zipEntry);
				if (zipEntry.isDirectory()) {
					if (!newFile.isDirectory() && !newFile.mkdirs()) {
						throw new IOException("Failed to create directory " + newFile);
					}
				} else {
					File parent = newFile.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("Failed to create directory " + parent);
					}

					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Launcher.download.sendPopup("Cache updated.");
	}
}
