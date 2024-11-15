package com.fox.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;

import com.fox.Settings;
import com.tarn.Configuration;

@SuppressWarnings("serial")
public class Control extends JButton implements MouseListener {
	
	public Control(Icon image) {
		super(image);
		
		setBorderPainted(false);
		setFocusable(false);
		addMouseListener(this);
	}
	
	public Control(String name) {
		super(name);
		setForeground(Configuration.buttonDefaultColor);
		setBorderPainted(false);
		setFocusable(false);
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		setBackground(this.getBackground().darker());
	}

	@Override
	public void mouseExited(MouseEvent me) {
		setBackground(this.getBackground().brighter());
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
