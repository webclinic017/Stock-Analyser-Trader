package com.gui;

import com.analyzer.backtesting.CrossoverTester;
import com.analyzer.tools.EMA;
import com.analyzer.tools.SMA;
import com.api.RequestHandler;
import com.asset.Asset;
import com.utils.FileHandler;
import com.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class SimulateGraphically extends JPanel {
    // canvas for other GUI widgets

    int width, height;
    Asset asset;

    FileHandler FileHandler = new FileHandler();

    JButton button;
    JTextField textfield;
    JLabel name, info, icon, chart;

    // for paint canvas - putting it here for scopes
    ArrayList<Float> close_prices = new ArrayList<>();
    ArrayList<Float> ma1_data = new ArrayList<>();
    ArrayList<Float> ma2_data = new ArrayList<>();

    float highest_data_point;
    float previous_highest_data_point;

    // making them non-primitive to hold null
    Boolean ma_crossover_buy_state; // true if sma1 > sma2
    Boolean previous_ma_crossover_buy_state;

    String maType1, maType2;
    String ma1LabelLine = "data/default/red-line.png";
    String ma2LabelLine = "data/default/green-line.png";
    Color ma1Color = Color.RED;
    Color ma2Color = Color.GREEN;

    JLabel tradesExec;
    JLabel tradeslabel;
    JScrollPane scrollableTextArea;

    // TODO: Add a iframe and embed tradingview
    public SimulateGraphically(int width, int height, Asset asset, int ma1, int ma2, String type1, String type2) throws Exception {
        this.width = width;
        this.height = height;
        this.asset = asset;
        this.maType1 = type1.toLowerCase();
        this.maType2 = type2.toLowerCase();
        if (maType1.equals("ema")) {ma1Color = Color.BLUE; ma2Color = Color.RED; ma1LabelLine = "data/default/blue-line.png"; ma2LabelLine = "data/default/red-line.png";}

        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);


        JLabel label = new JLabel("MA Crossover");
        label.setFont(new Font("Verdana", Font.BOLD, 20));
        label.setBounds(70, 50, 300, 50);
        add(label);

        JLabel type = new JLabel("Backtesting");
        type.setFont(new Font("Verdana", Font.BOLD, 12));
        type.setBounds(70, 75, 150, 50);
        add(type);

        JLabel icon = new JLabel(asset.icon);
        icon.setIcon(new ImageIcon(asset.icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
        icon.setBounds(70,120, 40, 40);
        add(icon);

        JLabel name = new JLabel(asset.name);
        name.setFont(new Font("Verdana", Font.BOLD, 15));
        name.setBounds(120, 115, 350, 50);
        add(name);

        JLabel buy = new JLabel("Buy");
        buy.setIcon(new ImageIcon(new ImageIcon("data/default/profit.png").getImage().getScaledInstance(8, 8, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        buy.setBounds(470, 550, 150, 15);
        add(buy);

        JLabel shortsell = new JLabel("Short");
        shortsell.setIcon(new ImageIcon(new ImageIcon("data/default/loss.png").getImage().getScaledInstance(8, 8, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        shortsell.setBounds(470, 570, 150, 15);
        add(shortsell);

        JLabel ma1label = new JLabel(maType1.toUpperCase() + " (Short) --> " + ma1);
        ma1label.setFont(new Font("Verdana", Font.BOLD, 12));
        ma1label.setIcon(new ImageIcon(new ImageIcon(ma1LabelLine).getImage().getScaledInstance(30, 12, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        ma1label.setBounds(380, 120, 200, 30);
        add(ma1label);

        JLabel ma2label = new JLabel(maType2.toUpperCase() + " (Long)  --> " + ma2);
        ma2label.setFont(new Font("Verdana", Font.BOLD, 12));
        ma2label.setIcon(new ImageIcon(new ImageIcon(ma2LabelLine).getImage().getScaledInstance(30, 12, Image.SCALE_DEFAULT))); // scaling the image properly so that there is no stretch
        ma2label.setBounds(380, 140, 200, 30);
        add(ma2label);



        // TODO: Show an animation of this as it's happening... and a new thread will do the normal .simulate() call...
        // TODO: will keep the user busy while the real whole simulation runs... ESPECIALLY FOR THE 1 MIN ONES
//        int[][] sma_to_show = {{20,50}, {50,180}};

//        for (int[] ints : sma_to_show) {
//            int ma1 = ints[0];
//            int ma2 = ints[1];
//
//            float[] result = smaCrossoverTester.test(ma1, ma2, false);
//            float gain = result[0];
//            System.out.println(ma1 + " " + ma2);
//            System.out.println(gain);
//            System.out.println(result[1]);
//        }


        // TODO: Animate the stock close price like every 5 days or so, then also animate the sma lines, when there's a trade draw a vertical line of color...
        // TODO: Then below that stock graph, have a portfolio graph which too updates in the same frequency, if profit draw the lines in green if goes to loss draw the lines in red...

        ArrayList<Float> all_historical_data = asset.getHistorical_data(5); // getting 5 - close price



        System.out.println(ma1 + " , " + ma2);

        ArrayList<Float> all_ma_data_1 =  new ArrayList<>();
        ArrayList<Float> all_ma_data_2 =  new ArrayList<>();


        if (maType1.equals("ema")) {
            EMA MA_1 = new EMA(ma1);
            all_ma_data_1 = MA_1.calculateEMA(asset);
        } else if (maType1.equals("sma")) {
            SMA MA_1 = new SMA(ma1);
            all_ma_data_1 = MA_1.getSMAData(asset);
        }

        if (maType2.equals("ema")) {
            EMA MA_2 = new EMA(ma2);
            all_ma_data_2 = MA_2.calculateEMA(asset);
        } else if (maType2.equals("sma")) {
            SMA MA_2 = new SMA(ma2);
            all_ma_data_2 = MA_2.getSMAData(asset);
        }



        // TODO: this is only useful if using simulation over it's lifetime and see how it performed in the 2 years
        // TODO: mention this : stripping the data points of historical data points to the last 550 data points, this means ma1 and ma2 also no longer start from the bottom as they would have been calculated from previous time frames already
        // stripping the data points to the last 550 data points - that 2 years and about 3 months, fits perfectly in the screen
        ArrayList<Float> historical_data = Utils.stripArrayList(all_historical_data, 550, false);
        ArrayList<Float> ma_data_1 = Utils.stripArrayList(all_ma_data_1, 550, false);
        ArrayList<Float> ma_data_2 = Utils.stripArrayList(all_ma_data_2, 550, false);



//        // Found this... source : https://stackoverflow.com/a/21801845 modified for my needs by creating a separate Actionlistner because I want to access the timer variable from inside
        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            int counter = 0; // counting the iterations to get the index of the data for next loop
            public void actionPerformed(ActionEvent e) {
                if (false) { // when you want to stop it...
                    ((Timer) e.getSource()).stop();
                } else {

                    try {
                        close_prices.add(historical_data.get(counter));
                        ma1_data.add(ma_data_1.get(counter));
                        ma2_data.add(ma_data_2.get(counter));

                        counter++;
                        repaint();
                    } catch (Exception error){
                        tradeslabel.setVisible(true);
                        scrollableTextArea.setVisible(true);
                        repaint();
                        System.out.println("Simulation finished");
                        timer.stop();
                    }

                }
            }
        });



        JButton start = new JButton("Visualise");
        start.setBounds(70,180,90,25);

        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });
        add(start);

        JButton autoTrade = new JButton("Auto-Trade");
        autoTrade.setBounds(170,180,100,25);

        autoTrade.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RequestHandler requestHandler = new RequestHandler();
                try {
                    requestHandler.get("http://localhost:5000/trader?symbol="+asset.ticker+"&ma1="+ma1+"&ma2="+ma2+"&ma1-type="+maType1+"&ma2-type="+maType2, false);
                    autoTrade.setText("Auto-Trading");
                    autoTrade.setIcon(new ImageIcon(new ImageIcon("data/default/tick.png").getImage().getScaledInstance(10, 15, Image.SCALE_SMOOTH))); // scaling the image properly so that there is no stretch
                    autoTrade.setBounds(200,180,122,25);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // JOptionPane.showMessageDialog(this, "Unsuccessful, the server is down");
                }
            }
        });
        add(autoTrade);

        tradeslabel = new JLabel("Executed Trades");
        tradeslabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        tradeslabel.setBounds(610, 140, 300, 50);
        tradeslabel.setVisible(false);
        add(tradeslabel);


        CrossoverTester crossoverTester = new CrossoverTester(asset, type1, type2);
        crossoverTester.test(ma1, ma2, true); // logging the trades: true

        // trades TODO: FORMAT THESE A BIT
        String filename = "data/stock/"+asset.ticker+"/ma-crossover-trades-"+asset.historicalDataTimeframe+"-"+maType1+"-"+ma1+"-"+maType2+"-"+ma2+".csv";

        String[][] tradesExecuted = Utils.convertToMultiDArrayFromCSV(filename, 4);

        StringBuilder tradesExecutedString = new StringBuilder();

        for (int i = 0; i<tradesExecuted.length; i++){
            String tradeType = tradesExecuted[i][1];
            try {
                tradeType = tradeType.replace("SHORT-COVER/BUY", "BUY");
                tradeType = tradeType.replace("SELL/SHORT", "SHORT");

                String text = tradesExecuted[i][0] + ", " + tradeType + " @ " + tradesExecuted[i][2] + " " + Utils.paddGain(tradesExecuted[i][3]);
                tradesExecutedString.append(text + "<br>");
            }  catch (Exception ignored){}

        }

        tradesExec = new JLabel();
        tradesExec.setText("<html>" + tradesExecutedString + "</html>");
//        tradesExec.setMargin(new Insets(5,5,5,5)); // padding the text
//        tradesExec.setEditable(false);
//        tradesExec.setBounds(610, 180, 250, 400);
//        add(tradesExec);



        scrollableTextArea = new JScrollPane(tradesExec);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setBounds(610, 180, 350, 400);
        scrollableTextArea.setVisible(false);
        add(scrollableTextArea);


    }

    // Basically calculates the sma crossover over and shows the results on screen as it does it...
    // TODO: name the method as simulateSMACrossover
    // TODO: do the test() function but in one for loop?
    // TODO: there is no need copy paste the test() code, you just find the sma1 and sma2 and loop through the price array, then on each loop count, check it sma crossover or not, if happens, draw the vertical line, take the trade, then update the money value every day after based on the price of the stock, then chart the stock price, the smas and below chart the value of the money as the days pass...
    public void paintComponent(Graphics g) {

        // TODO: consumes CPU for no reason
        super.paintComponent(g);
        repaint();

//        super.paintComponent(g);
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, getWidth(), getHeight());
//        for (Line line : lines) {
//            // TODO: Draw a horizontal line at the 0 point in the middle of the screen...
//            // TODO: probably make the line a 2D array, one the value and another one the colour, as profit (above the zero horizontal line) will be green and loss red
//            line.drawLine(g);
//        }
//

        // checking if need to clear the panel to avoid multiple lines from appearing...
        if (highest_data_point != previous_highest_data_point){
            super.paintComponent(g);
            repaint();
            previous_highest_data_point = highest_data_point;
        }

        if (!close_prices.isEmpty()) {

            float[] highest_and_lowest = Utils.findHighestAndLowest(close_prices);

            highest_data_point = highest_and_lowest[0];
            float lowest_data_point = highest_and_lowest[1];

            int max_y_point = 630; // getting the point in the middle... making that tha base...
            float previous_close = (close_prices.get(0) / highest_data_point * (height / 2)) + 10; // getting the first data point as previous close so that it doesn't start from 0
            float previous_ma1 = (ma1_data.get(0) / highest_data_point * (height / 2)) + 10;
            float previous_ma2 = (ma2_data.get(0) / highest_data_point * (height / 2)) + 10;

            int day_counter = 20; // padding of 20 days to the left of screen in GUI

            for (int i = 0; i<close_prices.size(); i++) {
                // printing the stock price
                float close_price = (close_prices.get(i) / highest_data_point * (height / 2)) + 10; // 5 for close price
                g.setColor(Color.BLACK);
                g.drawLine(day_counter, max_y_point - (int) previous_close, day_counter + 1, max_y_point - (int) close_price); // TODO: adjust the +2 based on the number of data points
                previous_close = close_price;


                // printing the sma1
                float current_ma1 = (ma1_data.get(i) / highest_data_point * (height / 2)) + 10; // 5 for close price
                g.setColor(ma1Color);
                g.drawLine(day_counter, max_y_point - (int) previous_ma1, day_counter + 1, max_y_point - (int) current_ma1); // TODO: adjust the +2 based on the number of data points
                previous_ma1 = current_ma1;


                // printing the sma2
                float current_ma2 = (ma2_data.get(i) / highest_data_point * (height / 2)) + 10; // 5 for close price
                g.setColor(ma2Color);
                g.drawLine(day_counter, max_y_point - (int) previous_ma2, day_counter + 1, max_y_point - (int) current_ma2); // TODO: adjust the +2 based on the number of data points
                previous_ma2 = current_ma2;


                // drawing the horizontal lines for crossovers
                // TODO: IMPROVE THIS CAUSE IT JUST BUYS AT FIRST
                if (current_ma1 > current_ma2){
                    ma_crossover_buy_state = true;
                } else {
                    ma_crossover_buy_state = false;
                }

                if (ma_crossover_buy_state != previous_ma_crossover_buy_state){ // then crossover happened
                    if (ma_crossover_buy_state) { // if it changed to true, it means buy state
                        System.out.println("BUY");
                        System.out.println(close_price);
                        g.setColor(Color.GREEN);
//                        g.drawLine(day_counter,0, day_counter, height);

                        g.drawPolygon(new int[] {day_counter-5, day_counter, day_counter+5}, new int[] {(int) (max_y_point-current_ma1+5), (int) (max_y_point-current_ma1-5), (int) (max_y_point-current_ma1+5)}, 3);
                        g.setColor(Color.green);
                        g.fillPolygon(new int[] {day_counter-5, day_counter, day_counter+5}, new int[] {(int) (max_y_point-current_ma1+5), (int) (max_y_point-current_ma1-5), (int) (max_y_point-current_ma1+5)}, 3);

//                        JLabel label = new JLabel("Buy");
//                        label.setFont(new Font("Verdana", Font.PLAIN, 8));
//                        label.setBounds(day_counter-8, (int) (max_y_point-current_ma1-5), 150, 50);
//                        add(label);

                    } else { // if current was false, it's a short
                        System.out.println("SHORT");
                        System.out.println(close_price);
                        g.setColor(Color.RED);
//                        g.drawLine(day_counter,0, day_counter, height);

                        g.drawPolygon(new int[] {day_counter-5, day_counter, day_counter+5}, new int[] {(int) (max_y_point-current_ma1-5), (int) (max_y_point-current_ma1+5), (int) (max_y_point-current_ma1-5)}, 3);
                        g.setColor(Color.red);
                        g.fillPolygon(new int[] {day_counter-5, day_counter, day_counter+5}, new int[] {(int) (max_y_point-current_ma1-5), (int) (max_y_point-current_ma1+5), (int) (max_y_point-current_ma1-5)}, 3);

//                        JLabel label = new JLabel("Short");
//                        label.setFont(new Font("Verdana", Font.PLAIN, 8));
//                        label.setBounds(day_counter-8, (int) (max_y_point-current_ma1-5), 150, 50);
//                        add(label);

                    }
                }

                previous_ma_crossover_buy_state = ma_crossover_buy_state;
                day_counter++;

            }
        }

    }
}