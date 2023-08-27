package com.tarn.frame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.fox.Settings;
import com.fox.components.Control;
import com.fox.components.IconLabel;
import com.fox.components.MenuBar;
import com.fox.listeners.ButtonListener;
import com.fox.listeners.HoverListener;
import com.fox.threads.ServerTime;
import com.tarn.Configuration;
import com.tarn.io.UpdateChecker;
import com.tarn.utils.Utils;

public class AppFrame extends JFrame {
	private static Point initialClick;
	
	public static int appWidth, appHeight;
	public static String serverStatus = "...";
	public static JProgressBar pbar, clBar, caBar, jaBar;
	public static Control playButton = new Control("Launch"), clientUpdate, cacheUpdate, javaUpdate;

	public static JLabel tooltip, infoTip;
	public static IconLabel serverTime, playerCount;

	public static Label clients, version, cache, cacheVersion, java, javaVersion;

	public static ColorIcon icon;

	public static boolean isOnline = false;
	public static String[] info = new String[16];

	public static boolean cacheUpdated = false,  clientUpdated = false, javaUpdated = false;

	public static TrayIcon trayIcon;
	
	public AppFrame() {
		setPreferredSize(Configuration.frameSize);
		
		appWidth = (int) getPreferredSize().getWidth();
		appHeight = (int) getPreferredSize().getHeight();
		
		setUndecorated(true);
		setTitle(Settings.SERVER_NAME);
		setLayout(null);
		getRootPane().setBorder(new LineBorder(Color.BLACK));
		getContentPane().setBackground(Configuration.backgroundColor);

		try {
			trayIcon = new TrayIcon(Utils.getRotatedImage("favicon.png", 0), "Tarn");
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addMenuBar();
		addLinks();
		addHeader();
		addPlayButton();
		addProgressBar();

		drawStatus();

		startCheck();
		
		setIconImage(Utils.getImage("favicon_large.png").getImage());
		addMouseListener();
		pack();
	}

	private void startCheck(){
		clientChecker();
		cacheChecker();
		javaChecker();

		new Thread(new UpdateChecker()).start();
		new Thread(new ServerTime()).start();
	}

	private void clientChecker(){
		IconLabel checker = new IconLabel("Client Checker", 16);
		checker.setBounds(125, 140, 180, 24);
		JPanel panel = new JPanel();
		panel.setBounds(15, 170, 400, 60);
		panel.setForeground(Configuration.primaryColor);
		panel.setBackground(Configuration.backgroundColor.darker());

		clients = new Label("Client Location : ");
		clients.setBounds(10, 0, 600, 30);
		version = new Label("Client Version: ");
		version.setBounds(10, 400, 600, 30);

		panel.add(clients);
		panel.add(version);

		clientUpdate = new Control("Update");
		clientUpdate.setBounds(15, 230, 80, 35);
		clientUpdate.setActionCommand("updateClient");
		clientUpdate.setBackground(Configuration.primaryColor.darker());
		clientUpdate.addActionListener(new ButtonListener());
		clientUpdate.setEnabled(false);

		clBar = new JProgressBar();

		clBar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return Configuration.primaryColor;
			}
			protected Color getSelectionForeground() {
				return Configuration.primaryColor;
			}
		});

		clBar.setBounds(95, 230, 320, 35);
		clBar.setBackground(Configuration.backgroundColor.brighter());
		clBar.setBorderPainted(false);
		clBar.setStringPainted(true);
		clBar.setString(". . .");
		clBar.setValue(0);
		clBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		clBar.setForeground(new Color(25, 25, 25));
		Utils.setFont(clBar, "OpenSans-Regular.ttf", 13);

		add(checker);
		add(clientUpdate);
		add(panel);
		add(clBar);

	}

	private void cacheChecker(){
		IconLabel checker = new IconLabel("Cache Checker", 16);
		checker.setBounds(175, 275, 180, 24);
		JPanel panel = new JPanel();
		panel.setBounds(15, 310, 400, 60);
		panel.setForeground(Configuration.primaryColor);
		panel.setBackground(Configuration.backgroundColor.darker());

		cache = new Label("Cache Location : ");
		cache.setBounds(10, 0, 600, 30);
		cacheVersion = new Label("Cache Version: ");
		cacheVersion.setBounds(10, 400, 600, 30);

		panel.add(cache);
		panel.add(cacheVersion);

		cacheUpdate = new Control("Update");
		cacheUpdate.setBounds(15, 370, 80, 35);
		cacheUpdate.setActionCommand("updateCache");
		cacheUpdate.setBackground(Configuration.primaryColor.darker());
		cacheUpdate.addActionListener(new ButtonListener());
		cacheUpdate.setEnabled(false);

		caBar = new JProgressBar();

		caBar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return Configuration.primaryColor;
			}
			protected Color getSelectionForeground() {
				return Configuration.primaryColor;
			}
		});

		caBar.setBounds(95, 370, 320, 35);
		caBar.setBackground(Configuration.backgroundColor.brighter());
		caBar.setBorderPainted(false);
		caBar.setStringPainted(true);
		caBar.setString(". . .");
		caBar.setValue(0);
		caBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		caBar.setForeground(new Color(25, 25, 25));
		Utils.setFont(caBar, "OpenSans-Regular.ttf", 13);

		add(checker);
		add(cacheUpdate);
		add(panel);
		add(caBar);

	}

	private void javaChecker(){
		IconLabel checker = new IconLabel("Java Checker", 16);
		checker.setBounds(175, 415, 180, 24);
		JPanel panel = new JPanel();
		panel.setBounds(15, 450, 400, 60);
		panel.setForeground(Configuration.primaryColor);
		panel.setBackground(Configuration.backgroundColor.darker());

		java = new Label("Java Location : ");
		java.setBounds(10, 0, 400, 200);
		javaVersion = new Label("Java Version: ");
		javaVersion.setBounds(10, 200, 400, 30);

		panel.add(java);
		panel.add(javaVersion);

		javaUpdate = new Control("Update");
		javaUpdate.setBounds(15, 510, 80, 35);
		javaUpdate.setActionCommand("updateJava");
		javaUpdate.setBackground(Configuration.primaryColor.darker());
		javaUpdate.addActionListener(new ButtonListener());
		javaUpdate.setEnabled(false);

		jaBar = new JProgressBar();

		jaBar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return Configuration.primaryColor;
			}
			protected Color getSelectionForeground() {
				return Configuration.primaryColor;
			}
		});

		jaBar.setBounds(95, 510, 320, 35);
		jaBar.setBackground(Configuration.backgroundColor.brighter());
		jaBar.setBorderPainted(false);
		jaBar.setStringPainted(true);
		jaBar.setString(". . .");
		jaBar.setValue(0);
		jaBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		jaBar.setForeground(new Color(25, 25, 25));
		Utils.setFont(jaBar, "OpenSans-Regular.ttf", 13);

		add(checker);
		add(javaUpdate);
		add(panel);
		add(jaBar);

	}

	private void drawStatus(){
		icon = new ColorIcon(15, Color.red);

		if(Utils.hostAvailabilityCheck()){
			icon = new ColorIcon(15, Color.green);
			isOnline = true;
		}

		JLabel label = new JLabel(icon);
		label.setBounds(appWidth - 30, appHeight - 25, 15, 15);
		label.setName("status");
		label.addMouseListener(new HoverListener());

		infoTip = new JLabel("");
		infoTip.setBounds(appWidth - 100, appHeight - 50, 200, 15);
		infoTip.setForeground(Configuration.primaryColor);
		infoTip.setFont(new Font("OpenSans-Regular.ttf", Font.PLAIN, 12));
		infoTip.setVisible(false);

		add(label);
		add(infoTip);
	}
	
	private void addProgressBar() {
		pbar = new JProgressBar();
		
		pbar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() { 
				return Configuration.primaryColor;
		    }
			protected Color getSelectionForeground() { 
				return Configuration.primaryColor;
		    }
		});
		
		pbar.setBounds(100, appHeight - 35, appWidth - 160, 35);
		pbar.setBackground(Configuration.backgroundColor.darker());
		pbar.setBorderPainted(false);
		pbar.setStringPainted(true);
		pbar.setString("Checking updates . . .");
		pbar.setBorder(new EmptyBorder(0, 0, 0, 0));
		pbar.setForeground(new Color(25, 25, 25));
		Utils.setFont(pbar, "OpenSans-Regular.ttf", 13);
		add(pbar);

	}
	
	private void addPlayButton() {
		playButton.setActionCommand("play");
		playButton.setBackground(Color.red);
		playButton.addActionListener(new ButtonListener());
		playButton.setEnabled(false);
		// (appWidth / 2) - (167 / 2)
		playButton.setBounds(0, appHeight - 35, 100, 35);
		Utils.setFont(playButton, "OpenSans-Regular.ttf", 16);
		add(playButton);
	}
	
	private void addLinks() {
		tooltip = new JLabel("");
		tooltip.setBounds(appWidth - 335,  appHeight - 85, 335, 25);
		tooltip.setHorizontalAlignment(SwingConstants.CENTER);
		Utils.setFont(tooltip, "OpenSans-Light.ttf", 16);
		add(tooltip);

		IconLabel forum = new IconLabel("\uf086", "Community", 50);
		forum.setBounds(605, 195, 64, 64);
		add(forum);
		
		IconLabel shop = new IconLabel("\uf07a", "Shop", 50);
		shop.setBounds(725, 195, 64, 64);
		add(shop);
		
		IconLabel vote = new IconLabel("\uf046", "Vote", 50);
		vote.setBounds(845, 195, 64, 64);
		add(vote);
		
		serverTime = new IconLabel("Server Time: 1:50 PM", 12);
		serverTime.setBounds(15, appHeight - 60, 175, 18);
		add(serverTime);
	}
	
	private void addMenuBar() {
		MenuBar bar = new MenuBar(this);
		bar.setBounds(0, 0, appWidth, 25);
		add(bar);
	}

	public void sendNotification(String message){
		if (SystemTray.isSupported()) {
			trayIcon.displayMessage("Tarn Downloader", message, TrayIcon.MessageType.INFO);
		}
	}

	public void sendError(String message){
		if (SystemTray.isSupported()) {
			trayIcon.displayMessage("Tarn Downloader", message, TrayIcon.MessageType.ERROR);
		}
	}

	private void addHeader() {
		JLabel logo = new JLabel(Utils.getImage("banner.gif"));
		logo.setBounds(145, 35, 720, 90);
		add(logo);
		
		JLabel head = new JLabel(" ");
		head.setOpaque(true);
		head.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.2f));
		head.setBounds(-1, 25, appWidth, 114);
		add(head);
	}
	
	private void addMouseListener() {
		addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            initialClick = e.getPoint();
	            getComponentAt(initialClick);
	        }

	    });
		
		addMouseMotionListener(new MouseMotionAdapter() {
	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	
	        	int iX = initialClick.x;
	        	int iY = initialClick.y;
	        	
	        	if (iX >= 0 && iX <= getPreferredSize().getWidth() && iY >= 0 && iY <= 25) {
	        		int thisX = getLocation().x;
		            int thisY = getLocation().y;
		            
		            int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
		            int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

		            int X = thisX + xMoved;
		            int Y = thisY + yMoved;
		            setLocation(X, Y);
	        	}
	        }
	    });
	}

	private static class ColorIcon implements Icon {

		private final int size;
		private Color color;

		public ColorIcon(int size, Color color) {
			this.size = size;
			this.color = color;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(color);
			g2d.fillOval(x, y, size, size);
		}

		@Override
		public int getIconWidth() {
			return size;
		}

		@Override
		public int getIconHeight() {
			return size;
		}
	}
}
