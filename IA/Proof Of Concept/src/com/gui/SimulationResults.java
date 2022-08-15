package com.gui;

import com.analyzer.backtesting.SMACrossoverTester;
import com.asset.Asset;
import com.utils.FileHandler;
import com.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class SimulationResults extends JPanel {

    int width, height;
    Asset asset;

    FileHandler FileHandler = new FileHandler();

    // TODO: Add a iframe and embed tradingview
    public SimulationResults(int width, int height, Asset asset) throws Exception {
        this.width = width;
        this.height = height;
        this.asset = asset;

        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);


        JLabel label = new JLabel("Backtesting");
        label.setFont(new Font("Verdana", Font.BOLD, 20));
        label.setBounds(70, 40, 150, 50);
        add(label);

        JLabel type = new JLabel("SMA Crossover");
        type.setFont(new Font("Verdana", Font.BOLD, 12));
        type.setBounds(70, 65, 150, 50);
        add(type);


        JLabel icon = new JLabel(asset.icon);
        icon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        icon.setBounds(70,110, 40, 40);
        add(icon);

        JLabel name = new JLabel(asset.name);
        name.setFont(new Font("Verdana", Font.BOLD, 15));
        name.setBounds(120, 105, 350, 50);
        add(name);



        // export option for further processing in external pieces of software if preferred by the user

        JButton export = new JButton("Export");
//            results[i].setFont(new Font("Verdana", Font.BOLD,12));
        export.setIcon(new ImageIcon(new ImageIcon("data/default/export.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        export.setBounds(270, 60, 100, 30);
        export.setHorizontalAlignment(SwingConstants.LEFT);
        export.setContentAreaFilled(false);
        export.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showSaveDialog(null);

                File folderpath = f.getSelectedFile(); // returns the selected directory.
                System.out.println(folderpath);

                try {
                    FileHandler.copyFile(new File("data/stock/" + asset.ticker + "/simulation-sma.csv"), new File(folderpath + "/" + asset.ticker + "-simulation-results.csv"));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        add(export);


        // start_date and end_date selector

        JLabel start_time_l = new JLabel("Start time");
        start_time_l.setFont(new Font("Verdana", Font.PLAIN, 12));
        start_time_l.setBounds(70, 145, 150, 50);
        add(start_time_l);

        JTextField start_time = new JTextField("01/08/2020");
        start_time.setBounds(70, 180, 95, 20);
        add(start_time);

        JLabel end_time_l = new JLabel("End Time");
        end_time_l.setFont(new Font("Verdana", Font.PLAIN, 12));
        end_time_l.setBounds(180, 145, 150, 50);
        add(end_time_l);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        JTextField end_time = new JTextField(dtf.format(now));
        end_time.setBounds(175, 180, 95, 20);
        add(end_time);

        JButton reset = new JButton("Reset");
        reset.setIcon(new ImageIcon(new ImageIcon("data/default/redo.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        reset.setBounds(280, 177, 90, 25);
        add(reset);
        reset.setContentAreaFilled(false);
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = new Date(start_time.getText());
                    long start = date.getTime() / 1000;

                    date = new Date(end_time.getText());
                    long end = date.getTime() / 1000;

                    asset.getHistorical_data(start, end);
                    displayResults();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        // only show intraday option if it's a stock

        if (asset.type.equals("us_equity")) {

            JLabel line = new JLabel();
            line.setIcon(new ImageIcon(new ImageIcon("data/default/line.png").getImage().getScaledInstance(301, 5, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
            line.setBounds(70, 205, 301, 5);
            add(line);

            String[] options = {"60min", "30min", "15min", "5min", "1min"};

            JLabel intradayLabel = new JLabel("<html>Intraday<br>(2 years)</html>");
            intradayLabel.setFont(new Font("Verdana", Font.BOLD, 12));
            intradayLabel.setBounds(70, 215, 70, 30);
            add(intradayLabel);

            JLabel historyTime = new JLabel("Timeframe: ");
            historyTime.setFont(new Font("Ubuntu", Font.BOLD, 11));
            historyTime.setBounds(145, 215, 75, 30);
            add(historyTime);

            // https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
            JComboBox<String> timeframe = new JComboBox<>(options);
            timeframe.setBounds(210, 218, 60, 25);
            add(timeframe);



            JButton start = new JButton("Start");
            start.setBounds(280, 218, 90, 25);
            add(start);
            start.setContentAreaFilled(false);
            start.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        System.out.println("Getting data");
                        asset.getIntraDay(options[timeframe.getSelectedIndex()]);
                        System.out.println("Loading");
                        displayResults();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
        }

    }

    private String paddGain(String text){
        // padding the results
        float percentage_gain = Float.parseFloat(text);
        BigDecimal bd = new BigDecimal(percentage_gain);
        bd = bd.round(new MathContext(4)); // TODO: make this relative
        float rounded = bd.floatValue();

        String gain;

        // TODO: Use local files
        if (rounded > 0){
            gain = "<img src='" + new File("data/default/profit.png").toURI() + "' width='9' height='10'> " + rounded + " %";
        } else {
            gain = "<img src='" + new File("data/default/loss.png").toURI() + "' width='9' height='9'> " + rounded + " %";
        }
        return gain;
    }

    private void displayResults() throws Exception {

        SMACrossoverTester smaCrossoverTester = new SMACrossoverTester(asset);
        int[] data = smaCrossoverTester.simulate(); // does the simulation and saves it to a file...
        int bestsma1 = data[0];
        int bestsma2 = data[1];

        // file handler, read in the simulation results saved to a csv and show it in a table
        // converting into 2D parsed csv file
        String[][] simulation_results = Utils.convertToMultiDArrayFromCSV("data/stock/" + asset.ticker + "/simulation-sma.csv", 4);

        // Showing the best result
        JButton top = new JButton();
        top.setText("<html>" + bestsma1 + ", " + bestsma2 + ", " + paddGain(String.valueOf(data[2])) + ", " + data[3] + "</html>");
        top.setBounds(70,260, 300, 30);
        top.setHorizontalAlignment(SwingConstants.LEFT);
        top.setContentAreaFilled(false);
        top.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    GUICaller.Simulate(asset, bestsma1, bestsma2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(top);



        // Showing top 5 results...
        JButton[] results = new JButton[5];

        // top 5
        for (int i = 0; i<5; i++){

            results[i] = new JButton();
            String text = "<html>" + simulation_results[i][0] + ", " + simulation_results[i][1] + ", " + paddGain(simulation_results[i][2]) + ", " + simulation_results[i][3] + "</html>";
            results[i].setText(text);
//            results[i].setFont(new Font("Verdana", Font.BOLD,12));
            results[i].setBounds(70,(i*35)+295, 300, 30);
            results[i].setHorizontalAlignment(SwingConstants.LEFT);
            results[i].setContentAreaFilled(false);
            int current = i;
            results[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        GUICaller.Simulate(asset, Integer.parseInt(simulation_results[current][0]), Integer.parseInt(simulation_results[current][1]));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            add(results[i]);
        }
        repaint();
    }
}