package com.gui;

import com.asset.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomSimulation extends JPanel implements ActionListener{
    Asset asset;
    JButton button;
    JLabel sma1label, sma2label, asseticon;
    JTextField sma1, sma2;

    public CustomSimulation(int width, int height, Asset asset) {
        this.asset = asset;

        System.out.println("SEQUENCE: Custom Simulation");
        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);

        sma1label = new JLabel("SMA (Short)");
        sma1label.setBounds(45,30, 80, 25);
        add(sma1label);

        sma1 = new JTextField();
        sma1.setBounds(130,30, 40, 25);
        add(sma1);

        sma2label = new JLabel("SMA (Long)");
        sma2label.setBounds(45,60, 80, 25);
        add(sma2label);

        sma2 = new JTextField();
        sma2.setBounds(130,60, 40, 25);
        add(sma2);

        asseticon = new JLabel();
        asseticon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        asseticon.setBounds(185,15, 50, 80);
        add(asseticon);


        button = new JButton("Simulate");
        button.addActionListener(this);
        button.setBounds(63,93, 90, 25);
        add(button);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        int sma1_value = Integer.parseInt(sma1.getText());
        int sma2_value = Integer.parseInt(sma2.getText());

        new Thread(() -> { // TODO: mention in criterion, separates so can run multiple
            try {
                GUICaller.Simulate(asset, sma1_value, sma2_value);
            } catch (Exception ignored){
            }
        }).start();

    }
}