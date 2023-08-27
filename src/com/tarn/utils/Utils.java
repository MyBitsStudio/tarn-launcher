package com.tarn.utils;

import com.tarn.Configuration;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

	public static void main(String[] args) {
		System.out.println(getServerTime());
	}
	
	public static DateFormat df;
	
	public static String getServerTime() {
		if (df == null) {
			df = new SimpleDateFormat("h:mm:ss a");
			df.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
		}
		return df.format(new Date());
	}

	/**
	 * Checks if the server is online or offline by attempting to make a TCP
	 * connection
	 * 
	 * @return true if it connects (ie. it is online)
	 */
	public static boolean hostAvailabilityCheck() {
		try (Socket s = new Socket(Configuration.SERVER_IP, Configuration.SERVER_PORT)) {
			s.setTcpNoDelay(true);
			return true;
		} catch (IOException ex) {
			/* ignore */
		}
		return false;
	}

	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a custom font from the data/font folder. Font must be either otf or
	 * ttf.
	 * 
	 * @param fontName
	 * @param size
	 */
	public static void setFont(Component c, String fontName, float size) {
		try {
			Font font = Font.createFont(0, new File(Configuration.CACHE_HOME + "data/fonts/" + fontName));
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			genv.registerFont(font);
			font = font.deriveFont(size);
			c.setFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens the users browser and goes to the specified URL
	 * @param url
	 */
	public static void openWebpage(String url) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(new URL(url).toURI());
	        } catch (Exception e) {
	           // e.printStackTrace();
	        }
	    }
	}
	
	private static long timeCorrection;
	private static long lastTimeUpdate;

	public static synchronized long currentTimeMillis() {
		long l = System.currentTimeMillis();
		if (l < lastTimeUpdate)
			timeCorrection += lastTimeUpdate - l;
		lastTimeUpdate = l;
		return l + timeCorrection;
	}
	
	public static ImageIcon getImage(String name) {
		return new ImageIcon(Configuration.CACHE_HOME+ "data/img/" + name);
	}

	public static BufferedImage getRotatedImage(String name, double degrees) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(Configuration.CACHE_HOME + "data/img/" + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Utils().rotate(image, degrees);
	}

	public static ImageIcon rotatedIcon(String name, double degrees) {
		return new ImageIcon(getRotatedImage(name, degrees));
	}

	public static ImageIcon scaleAndRotateImage(String name, double degrees, int width, int height) {
		BufferedImage image = Utils.getRotatedImage(name, degrees);
		image = Scalr.resize(image,
				Scalr.Method.SPEED,
				Scalr.Mode.AUTOMATIC,
				width,
				height);
		return new ImageIcon(image);
	}

	public BufferedImage rotate(BufferedImage image, Double degrees) {
		// Calculate the new size of the image based on the angle of rotaion
		double radians = Math.toRadians(degrees);
		double sin = Math.abs(Math.sin(radians));
		double cos = Math.abs(Math.cos(radians));
		int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
		int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

		// Create a new image
		BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotate.createGraphics();
		// Calculate the "anchor" point around which the image will be rotated
		int x = (newWidth - image.getWidth()) / 2;
		int y = (newHeight - image.getHeight()) / 2;
		// Transform the origin point around the anchor point
		AffineTransform at = new AffineTransform();
		at.setToRotation(radians, x + ((double) image.getWidth() / 2), y + ((double) image.getHeight() / 2));
		at.translate(x, y);
		g2d.setTransform(at);
		// Paint the originl image
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return rotate;
	}

	public static String formatMillisToDate(long millis) {
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
