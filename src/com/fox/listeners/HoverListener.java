package com.fox.listeners;

import com.tarn.frame.AppFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        switch(e.getComponent().getName()){
            case "status" :
                AppFrame.infoTip.setVisible(true);
                AppFrame.infoTip.setText("Status: "+(AppFrame.isOnline ? "Online" : "Offline"));
            break;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        switch(e.getComponent().getName()){
            case "status" :
                AppFrame.infoTip.setVisible(false);
                AppFrame.infoTip.setText("");
            break;
        }
    }
}
