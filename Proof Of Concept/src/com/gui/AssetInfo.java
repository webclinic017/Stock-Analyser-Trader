package com.gui;

// https://finnhub.io/docs/api/company-profile2


import com.asset.Asset;
import com.asset.Calendar;
import com.utils.FileHandler;
import com.utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;

// TODO: Useful info: https://www.cnbc.com/quotes/TSLA?tab=ownership & on Webull

public class AssetInfo extends JPanel {
    // canvas for other GUI widgets

    int width;
    int height;

    Asset asset;

    FileHandler FileHandler = new FileHandler();

    JButton button, simulate;
    JTextField textfield;
    JLabel name, info, icon, chart;

    // TODO: Add a iframe and embed tradingview 
    public AssetInfo(int width, int height, Asset asset) throws Exception {
        this.width = width;
        this.height = height;
        this.asset = asset;

        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);


        icon = new JLabel(asset.icon);
        icon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        icon.setBounds(70,50, 64, 64);
        add(icon);

        name = new JLabel(asset.name);
        name.setFont(new Font("Verdana", Font.BOLD, 17));
        name.setHorizontalAlignment(SwingConstants.LEFT);
        name.setBounds(170, 35, 500, 40);
        add(name);

        String aboutStock = "Exchange: " + asset.exchange + ", " + asset.country + "<br>" +
                "Industry: " + asset.industry + "<br>" +
                "Sector: " + asset.sector + "<br>" +
                "IPO: " + asset.ipo + "<br>";

        String technicalAbout = "EPS: " + asset.EPS + "<br>" +
                "PERatio: " + asset.PERatio + "<br>" +
                "52 Week Low: " + asset.yearLow + "<br>52 Week High: " + asset.yearHigh;

        JLabel aboutStockLabel = new JLabel("<html>"+aboutStock+"</html>");
        aboutStockLabel.setHorizontalAlignment(SwingConstants.LEFT);
        aboutStockLabel.setBounds(170, 60, 500, 100);
        add(aboutStockLabel);

        JLabel technicalAboutLabel = new JLabel("<html>Financials<hr>"+technicalAbout+"</html>");
        technicalAboutLabel.setHorizontalAlignment(SwingConstants.LEFT);
        technicalAboutLabel.setBounds(350, 60, 500, 100);
        add(technicalAboutLabel);


        // TODO: worth mentioning in Criterion C?
        if ((asset.type.equals("us_equity") && Calendar.isMarketOpen()) || asset.type.equals("crypto")){
            JLabel price = new JLabel("Price: $" + asset.price());
            price.setFont(new Font("Verdana", Font.BOLD,13));
            price.setHorizontalAlignment(SwingConstants.LEFT);
            price.setBounds(170, 160, 200, 15);
            add(price);
        } else if(!Calendar.isMarketOpen() && asset.type.equals("us_equity")){
            JLabel price = new JLabel("Close Price: $" + asset.price());
            price.setFont(new Font("Verdana", Font.BOLD,13));
            price.setHorizontalAlignment(SwingConstants.LEFT);
            price.setBounds(170, 160, 200, 15);
            add(price);

        }

        // TODO: I say use the python, more complexity...
//        if (asset.about != null){
//            JLabel about = new JLabel("<html>" + asset.about + "</html>");
//            about.setBounds(170, 120, 300, 70);
//            add(about);
//        }



        // TODO: Organise the action listener, make the class implement Action Listener
        simulate = new JButton("Simulate"); // TODO: or call it Backtesting/Backtracking?
        simulate.setFont(new Font("Verdana", Font.BOLD,12));
        simulate.setBounds(170,190, 95, 25);

        simulate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try {
                    GUICaller.SimulationResults(Asset.create(asset.ticker)); // we want a new instantiation of a class every time as the historical data is manipulated
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        add(simulate);


    }


    // TODO: make a algorithm that limits the lines from shooting off the graph, limit the max x and y values according to the prices overtime
    // TODO: for eg: BTC's price of 30,000 shoots off the graph... make it relative, probably graph the percentage increase?
    // TODO: find the highest and lowest point, plot the percentage increase
    // TODO: just find the higest point and divide everything by it...
    public void paintComponent(Graphics g){ // paints the stock chart

        ArrayList<Float> all_historical_data = asset.getHistorical_data(5);
        ArrayList<Float> historical_data = Utils.stripArrayList(all_historical_data, 550, false);


        float[] highest_and_lowest = Utils.findHighestAndLowest(asset.getHistorical_data(5));

        float highest_data_point = highest_and_lowest[0];
        float lowest_data_point = highest_and_lowest[1];

        int max_y_point = 630; // getting the point in the middle... making that tha base...
        float previous_close = (historical_data.get(0) /highest_data_point * (height/2))+10; // getting the first data point as previous close so that it doesn't start from 0
        int day_counter = 20;

        for (Float daily_data : historical_data) {
            float close_price = (daily_data/highest_data_point * (height/2))+10; // 5 for close price
            g.drawLine(day_counter, max_y_point-(int) previous_close, day_counter+1, max_y_point-(int) close_price); // TODO: adjust the +2 based on the number of data points

            previous_close = close_price;
            day_counter++;
        }
    }
}
