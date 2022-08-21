package com.gui;

import com.asset.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomSimulation extends JPanel implements ActionListener{
    Asset asset;
    JButton button;
    JLabel ma1label, ma2label, asseticon;
    JTextField sma1, sma2;
    JComboBox maType1, maType2;

    public CustomSimulation(int width, int height, Asset asset) {
        this.asset = asset;

        System.out.println("SEQUENCE: Custom Simulation");
        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);

        ma1label = new JLabel("(Short)");
        ma1label.setBounds(35,30, 80, 25);
        add(ma1label);

        ma2label = new JLabel("(Long)");
        ma2label.setBounds(35,60, 80, 25);
        add(ma2label);

        String[] maOptions1 = {"EMA", "SMA"};
        maType1 = new JComboBox<>(maOptions1);
        maType1.setBounds(80,30, 55, 25);
        add(maType1);

        String[] maOptions2 = {"SMA", "EMA"};
        maType2 = new JComboBox<>(maOptions2);
        maType2.setBounds(80,60, 55, 25);
        add(maType2);


        sma1 = new JTextField();
        sma1.setBounds(145,30, 40, 25);
        add(sma1);

        sma2 = new JTextField();
        sma2.setBounds(145,60, 40, 25);
        add(sma2);

        asseticon = new JLabel();
        asseticon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        asseticon.setBounds(195,15, 50, 80);
        add(asseticon);

        button = new JButton("Simulate");
        button.addActionListener(this);
        button.setBounds(80,93, 90, 25);
        add(button);

    }


    @Override
    public void actionPerformed(ActionEvent e){
        int sma1_value = Integer.parseInt(sma1.getText());
        int sma2_value = Integer.parseInt(sma2.getText());

        new Thread(() -> { // TODO: mention in criterion, separates so can run multiple
            try {
                GUICaller.Simulate(asset, sma1_value, sma2_value, (String) maType1.getSelectedItem(), (String) maType1.getSelectedItem());
            } catch (Exception ignored){
            }
        }).start();

    }
}