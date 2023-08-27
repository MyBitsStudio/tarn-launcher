package com.fox;

import java.awt.Color;

import javax.swing.UIManager;

import com.tarn.frame.AppFrame;
import com.fox.threads.ServerTime;

public class Launcher {

	public static AppFrame app;
	
	public static void main(String[] main) {
		UIManager.put("Button.select", new Color(1.0f,1.0f, 1.0f, 0.05f));
		System.setProperty("awt.useSystemAAFontSettings","on"); 
		System.setProperty("swing.aatext", "true");
		
		app = new AppFrame();
		app.setVisible(true);
		app.setLocationRelativeTo(null);


	}
	
	
}