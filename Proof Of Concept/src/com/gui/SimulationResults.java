package com.gui;

import com.analyzer.backtesting.CrossoverTester;
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
    JComboBox<String> maType1, maType2;

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
        label.setBounds(70, 20, 150, 50);
        add(label);

        JLabel type = new JLabel("MA Crossover");
        type.setFont(new Font("Verdana", Font.BOLD, 12));
        type.setBounds(70, 45, 150, 50);
        add(type);


        JLabel icon = new JLabel(asset.icon);
        icon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        icon.setBounds(70,90, 40, 40);
        add(icon);

        JLabel name = new JLabel(asset.name);
        name.setFont(new Font("Verdana", Font.BOLD, 15));
        name.setBounds(120, 80, 350, 50);
        add(name);

        JLabel ipo = new JLabel("IPO: " + asset.ipo);
        ipo.setFont(new Font("Verdana", Font.PLAIN, 9));
        ipo.setBounds(120, 95, 350, 50);
        add(ipo);



        // export option for further processing in external pieces of software if preferred by the user

        JButton export = new JButton("Export");
//            results[i].setFont(new Font("Verdana", Font.BOLD,12));
        export.setIcon(new ImageIcon(new ImageIcon("data/default/export.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        export.setBounds(270, 40, 100, 30);
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

        JLabel daytimeframelabel = new JLabel("1-day Timeframe");
        daytimeframelabel.setFont(new Font("Verdana", Font.BOLD, 12));
        daytimeframelabel.setBounds(70, 145, 150, 15);
        add(daytimeframelabel);

        JLabel start_time_l = new JLabel("Start time");
        start_time_l.setFont(new Font("Verdana", Font.PLAIN, 12));
        start_time_l.setBounds(70, 145, 150, 50);
        add(start_time_l);

        JTextField start_time = new JTextField("01/01/2020");
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

        JButton reset = new JButton("Start");
//        reset.setIcon(new ImageIcon(new ImageIcon("data/default/redo.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        reset.setBounds(280, 177, 90, 25);
        add(reset);
        reset.setContentAreaFilled(false);
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {

                    new Thread(() -> { // TODO: mention in criterion, separates so can run multiple
                        try {
                            Date date = new Date(start_time.getText());
                            long start = date.getTime() / 1000;

                            date = new Date(end_time.getText());
                            long end = date.getTime() / 1000;

                            if (end > start){ // end time needs to be greater than start time
                                asset.getHistorical_data(start, end);
                                displayResults();
                            }
                        } catch (Exception ignored){
                        }
                    }).start();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JLabel maoptionsLabel = new JLabel("MA (Short)    MA (Long)");
        maoptionsLabel.setBounds(240, 90, 200, 20);
        add(maoptionsLabel);


        String[] maOptions1 = {"EMA", "SMA"};
        maType1 = new JComboBox<>(maOptions1);
        maType1.setBounds(240, 110, 55, 20);
        add(maType1);

        String[] maOptions2 = {"SMA", "EMA"};
        maType2 = new JComboBox<>(maOptions2);
        maType2.setBounds(315, 110, 55, 20);
        add(maType2);



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
                        // TODO: Make a loading screen... probably just show a gif overwriting the entire screen, then when complete delete it...
//                        Icon imgIcon = new ImageIcon(this.getClass().getResource("default/simulating.gif"));
//                        JLabel label = new JLabel(imgIcon);
//                        label.setBounds(668, 43, 46, 14); // You can use your own values
//                        add(label);

                        new Thread(() -> { // TODO: mention in criterion, separates so can run multiple
                            try {
                                System.out.println("Getting data");
                                asset.getIntraDay(options[timeframe.getSelectedIndex()]);
                                System.out.println("Running Simulation");
                                displayResults();
                            } catch (Exception ignored){
                            }
                        }).start();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        JButton custom = new JButton("Run Custom");
        custom.setIcon(new ImageIcon(new ImageIcon("data/default/customise.png").getImage().getScaledInstance(17, 17, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        custom.setBounds(245,480, 125, 30);
        add(custom);
        custom.setContentAreaFilled(false);
        custom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    GUICaller.CustomizeSimluation(asset);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
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

        CrossoverTester crossoverTester = new CrossoverTester(asset, (String) maType1.getSelectedItem(), (String) maType2.getSelectedItem());
        System.out.println(maType1.getSelectedItem());
        System.out.println((String) maType2.getSelectedItem());
        int[] data = crossoverTester.simulate(); // does the simulation and saves it to a file...
        int bestma1 = data[0];
        int bestma2 = data[1];

        // file handler, read in the simulation results saved to a csv and show it in a table
        // converting into 2D parsed csv file
        String[][] simulation_results = Utils.convertToMultiDArrayFromCSV("data/stock/" + asset.ticker + "/simulation-ma.csv", 6);

        // Showing the best result
        JButton top = new JButton();
        top.setText("<html>" + bestma1 + ", " + bestma2 + ", " + paddGain(String.valueOf(data[2])) + ", " + data[3] + "</html>");
        top.setBounds(70,260, 300, 30);
        top.setHorizontalAlignment(SwingConstants.LEFT);
        top.setContentAreaFilled(false);
        top.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    GUICaller.Simulate(asset, bestma1, bestma2, (String) maType1.getSelectedItem(), (String) maType2.getSelectedItem());

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
            String text = "<html>" + simulation_results[i][1] + ", " + simulation_results[i][3] + ", " + paddGain(simulation_results[i][4]) + ", " + simulation_results[i][5] + "</html>";
            results[i].setText(text);
//            results[i].setFont(new Font("Verdana", Font.BOLD,12));
            results[i].setBounds(70,(i*35)+295, 300, 30);
            results[i].setHorizontalAlignment(SwingConstants.LEFT);
            results[i].setContentAreaFilled(false);
            int current = i;
            results[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        GUICaller.Simulate(asset, Integer.parseInt(simulation_results[current][1]), Integer.parseInt(simulation_results[current][3]), simulation_results[current][0], simulation_results[current][2]);
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