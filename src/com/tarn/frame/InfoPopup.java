package com.tarn.frame;

import javax.swing.*;
import java.util.Objects;

public class InfoPopup {

    private JDialog dialog;
    public void sendPopup(String info){
        if(!Objects.equals(dialog, null)){
            dialog.dispose();
        }
        dialog = new JDialog();
        String title = "Tarn Launcher";
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(10, 10, 260, 20);
        progressBar.setStringPainted(true);
        progressBar.setString(info);
        progressBar.setIndeterminate(true);
        dialog.add(progressBar);
        dialog.setVisible(true);
    }

    public void closePopup(){
        if(!Objects.equals(dialog, null)){
            dialog.dispose();
        }
    }

}
