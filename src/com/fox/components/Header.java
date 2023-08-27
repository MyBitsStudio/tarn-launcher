package com.fox.components;

import com.tarn.utils.Utils;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Header extends JLabel {

	public Header(String text) {
		super(text);
		setForeground(Color.WHITE);
		setHorizontalAlignment(SwingConstants.CENTER);
		Utils.setFont(this, "OpenSans-Light.ttf", 32);
	}
	
}
