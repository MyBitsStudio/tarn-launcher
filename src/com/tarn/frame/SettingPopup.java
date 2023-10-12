package com.tarn.frame;

import com.fox.listeners.ButtonListener;
import com.tarn.Configuration;

import javax.swing.*;
import java.util.Objects;

public class SettingPopup {

    private JDialog dialog;

    public void sendPopup(AppFrame frame){
        if(!Objects.equals(dialog, null)){
            dialog.dispose();
        }

        dialog = new JDialog();
        String title = "Launcher Settings";
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(frame);
        JButton auto = new JButton("Auto-Update: "+ (Configuration.autoUpdate ? "On" : "Off"));

        auto.setName("auto-update");
        auto.addActionListener(new ButtonListener());
        auto.setSize(300, 100);

        JButton update = new JButton("Auto-Check: "+ (Configuration.autoCheck ? "On" : "Off"));

        update.setName("auto-check");
        update.addActionListener(new ButtonListener());
        update.setSize(300, 100);
        update.setAlignmentY(150);

        dialog.add(auto);
        dialog.add(update);

        dialog.setVisible(true);
    }

    public void closePopup(){
        if(!Objects.equals(dialog, null)){
            dialog.dispose();
        }
    }
}
