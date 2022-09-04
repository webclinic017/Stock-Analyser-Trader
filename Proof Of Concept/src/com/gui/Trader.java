package com.gui;

import com.api.RequestHandler;
import com.asset.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Trader extends JPanel implements ActionListener{
    Asset asset;
    JButton button;
    JLabel ma1label, ma2label, asseticon;
    JTextField sma1, sma2;
    JComboBox maType1, maType2, timeframe;

    public Trader(int width, int height, Asset asset) {
        this.asset = asset;

        System.out.println("SEQUENCE: Trader");
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

        String[] options = {"1d", "60min", "30min", "15min", "5min", "1min"};

        JLabel historyTime = new JLabel("Timeframe ");
        historyTime.setBounds(15, 88, 75, 30);
        add(historyTime);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
        timeframe = new JComboBox<>(options);
        timeframe.setBounds(80, 90, 58, 25);
        add(timeframe);

        button = new JButton("Schedule");
        button.addActionListener(this);
        button.setBounds(145,93, 90, 25);
        add(button);

    }


    @Override
    public void actionPerformed(ActionEvent e){
        int ma1 = Integer.parseInt(sma1.getText());
        int ma2 = Integer.parseInt(sma2.getText());
        String chosentimeframe = (String) timeframe.getSelectedItem();

        try {
            asset.getIntraDay(chosentimeframe);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Thread(() -> { // TODO: mention in criterion, separates so can run multiple
            try {
                RequestHandler requestHandler = new RequestHandler();
                requestHandler.get("http://localhost:5000/trader?symbol="+asset.ticker+"&ma1="+ma1+"&ma2="+ma2+"&ma1-type="+maType1.getSelectedItem()+"&ma2-type="+maType2.getSelectedItem(), false);
            } catch (Exception ignored){
            }
        }).start();

    }
}