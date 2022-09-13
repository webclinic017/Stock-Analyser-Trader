package com.gui;

import com.analyzer.backtesting.CrossoverTester;
import com.api.FinnhubAPI;
import com.asset.Asset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MassSimulate extends JPanel implements ActionListener{
    Asset asset;
    JButton button;
    JLabel ma1label, ma2label, asseticon;
    JComboBox maType1, maType2, timeframe, massoptions;
    String[] options = {"NDX", "SPY"};

    StringBuilder results = new StringBuilder();

    public MassSimulate(int width, int height, Asset asset) {
        this.asset = asset;

        System.out.println("SEQUENCE: Mass Simulate");
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


        String[] timeframeOptions = {"1d", "60min", "30min", "15min", "5min", "1min"};

        JLabel historyTime = new JLabel("Timeframe ");
        historyTime.setBounds(15, 88, 75, 30);
        add(historyTime);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
        timeframe = new JComboBox<>(timeframeOptions);
        timeframe.setBounds(80, 90, 58, 25);
        add(timeframe);

        massoptions = new JComboBox<>(options);
        massoptions.setBounds(150,30, 55, 25);
        add(massoptions);

        button = new JButton("Simulate");
        button.addActionListener(this);
        button.setBounds(145,93, 90, 25);
        add(button);

        JLabel label = new JLabel();
        label.setText("<html><font color='red'>WARNING: USES HIGH RESOURCES</font></html>");
        label.setBounds(25,105, 300, 50);
        add(label);

    }


    @Override
    public void actionPerformed(ActionEvent e){

        String chosentimeframe = (String) timeframe.getSelectedItem();

        String[] tickers = FinnhubAPI.constituents((String) massoptions.getSelectedItem());

        String type1 = (String) maType1.getSelectedItem();
        String type2 = (String) maType2.getSelectedItem();

        for (String ticker: tickers){
            new Thread(() -> {
                try {
                    Asset asset = Asset.create(ticker);
                    asset.getIntraDay(chosentimeframe);
                    CrossoverTester crossoverTester = new CrossoverTester(asset, type1, type2);
                    int[] res = crossoverTester.simulate();
                    results.append("MA-Crossover, "+type1+", "+res[0]+", "+type2+", "+ res[1]+", "+res[2]+ ", "+ res[3]);

                } catch (Exception error) {
                    error.printStackTrace();
                }
            }).start();
        }
    }
}