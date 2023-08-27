package com.fox.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fox.Settings;
import com.fox.listeners.ButtonListener;
import com.tarn.Configuration;
import com.tarn.frame.AppFrame;
import com.tarn.utils.Utils;

public class MenuBar extends JPanel {
	
	public MenuBar(AppFrame frame) {
		setLayout(null);
		
		setBounds(0, 0, AppFrame.appWidth, 25);
		setBackground(Configuration.primaryColor);
		
		Control exit = new Control("X");
		exit.setBackground(Configuration.primaryColor);
		exit.addActionListener(new ButtonListener());
		exit.setBounds(AppFrame.appWidth - 50, 0, 50, 25);
		add(exit); 
		
		Control mini = new Control("_");
		mini.setBackground(Configuration.backgroundColor);
		mini.addActionListener(new ButtonListener());
		mini.setBounds(AppFrame.appWidth - 100, 0, 50, 25);
		add(mini);

		Control help = new Control("?");
		help.setBackground(Configuration.helpColor);
		help.addActionListener(new ButtonListener());
		help.setBounds(AppFrame.appWidth - 150, 0, 50, 25);
		add(help);
		
		JLabel title = new JLabel(Configuration.SERVER_NAME+" Launcher");
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		Utils.setFont(title, "OpenSans-Regular.ttf", 14);
		title.setBounds(0, 0, AppFrame.appWidth, 25);
		
		JLabel icon = new JLabel(Utils.getImage("favicon.gif"));
		icon.setBounds(3, -2, 24, 28);
		add(icon);
		add(title);
	}
	
}
