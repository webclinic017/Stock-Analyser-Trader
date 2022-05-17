package com.gui;

import com.stock.Asset;
import com.stock.Crypto;
import com.stock.Forex;
import com.stock.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Considering making this the home page so will include other info as well...
public class StockChooser extends JPanel {
    // canvas for other GUI widgets
    GUICaller GUICaller = new GUICaller();

    JButton button;
    JTextField textfield;
    JLabel label;
    JLabel label1;

    public StockChooser(int width, int height) {
        System.out.println("SEQUENCE: GUI constructor");
        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);
        button = new JButton("Search");
        button.setBounds(170,10, 80, 30);

        textfield = new JTextField();
        textfield.setBounds(50,12, 100, 30);

        label = new JLabel("");
        label.setBounds(10,50, 1000000, 40);

        label1 = new JLabel("");
        label1.setBounds(10,70, 1000000, 40);


        add(button);
        add(textfield);
        add(label);
        add(label1);

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                String textFieldValue = textfield.getText().toUpperCase();
                System.out.println(textFieldValue);

                try {
                    String type = Asset.type(textFieldValue);
                    Asset asset = switch (type) {
                        case "crypto" -> new Crypto(textFieldValue);
                        case "us_equity" -> new Stock(textFieldValue); // normal stock essentially...
                        default -> new Forex(textFieldValue);
                    };

                    GUICaller.StockInfo(asset);

                } catch (Exception e) {
                    System.out.println("Stock Doesn't Exists"); // TODO: check if it's true if not print the error message
                    System.out.println(e);
                }
            }
        });
    }
}