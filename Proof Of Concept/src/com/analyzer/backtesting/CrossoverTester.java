package com.analyzer.backtesting;

import com.analyzer.tools.EMA;
import com.analyzer.tools.SMA;
import com.asset.Asset;
import com.utils.FileHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

// TODO: Might wanna log the calculations to a csv file so that it can be processed in exel if wanted to
// TODO: save the things to be logged to a arraylist then pass it to a thread to save to a file...
public class CrossoverTester {
    private Asset asset;
    private Float[][] historicalData;
    private FileHandler fileHandler = new FileHandler();

    String ticker;
    String type1, type2;


    public CrossoverTester(Asset asset, String type1, String type2) {
        this.asset = asset;
        this.ticker = asset.ticker;
        this.historicalData = asset.historical_data;
        this.type1 = type1.toLowerCase();
        this.type2 = type2.toLowerCase();
    }

    // Simulating the data to figure out the gain made... if had bought at the closing price
    // TODO: make it accept other values such as opening price or average day price...
    // returns the total_gain with number of trades made...

    // TODO: SEPERATE THE ACTUAL CROSSOVER PART INTO A NEW CLASS TO MAKE IT NEATER, SO LIKE GIVE IT A INDEX OF DAY AND IT WILL RETURN BOOLEAN IF CROSSOVER OR NOT
    public float[] test(int ma1, int ma2, boolean log_trades) throws Exception {

        String currentHoldingPositionType = ""; // to calculate daily profit
        StringBuilder eachCloseProfit = new StringBuilder();
        float totalCloseUnrealisedProfit = 1; // starting at 100% of the portfolio size

        float total_gain = 0;
        int numbers_of_trades = 0;

        StringBuilder buy_sell_log = new StringBuilder();
        String log = "";

        ArrayList<Float> ma_data_1 = new ArrayList<>();
        ArrayList<Float> ma_data_2 = new ArrayList<>();

        // SETTING MOVING AVERAGE VALUES AND CALCULATING DATA

        if (type1.equals("ema")) {
            EMA MA_1 = new EMA(ma1);
            ma_data_1 = MA_1.calculateEMA(asset);
        } else if (type1.equals("sma")) {
            SMA MA_1 = new SMA(ma1);
            ma_data_1 = MA_1.getSMAData(asset);
        }

        if (type2.equals("ema")) {
            EMA MA_2 = new EMA(ma2);
            ma_data_2 = MA_2.calculateEMA(asset);
        } else if (type2.equals("sma")) {
            SMA MA_2 = new SMA(ma2);
            ma_data_2 = MA_2.getSMAData(asset);
        }


//        System.out.println(Arrays.deepToString(ma_data_1.toArray()));
//        System.out.println(Arrays.deepToString(ma_data_2.toArray()));

        int total_data_size = ma_data_1.size();

//        System.out.println(ma1 + " : " + ma2);

        // creating an array with info if the ma1 was above or below ma2
        Boolean[] crossover = new Boolean[total_data_size];
        for (int i = 0; i < total_data_size; i++){
            if (ma_data_1.get(i) > ma_data_2.get(i)){
                crossover[i] = true;
            } else {
                crossover[i] = false;
            }
        }

        // checking whether it's buy or sell point if any
        Integer[] buy_sell = new Integer[total_data_size];
        for (int i = 0; i < total_data_size-1; i++){ // until the second last index as next is i+1
            boolean previous = crossover[i];
            boolean next = crossover[i+1];

            if (previous != next){
                if (previous == true & next == false){ // if small crosses the higher, sell
                    buy_sell[i] = 0; // 1 - buy & 0 - sell
                    numbers_of_trades += 2; // because it's first sell the current position, then short the stock

                } else if (previous == false & next == true){ // if small crosses the higher, buy
                    buy_sell[i] = 1;
                    numbers_of_trades += 2; // because it's first cover the short position, then buy the stock

                } else {
                    buy_sell[i] = -1; // if the same, nothing
                }
            }
        }


//        // printing the final results
//        for (int i = 0; i < total_data_size; i++){
//            System.out.println(ma_data_1.get(i) + " : " + ma_data_2.get(i) + " - " + crossover[i] + " -------- " + buy_sell[i]);
//        }


        // Calculating the total gain made...
        // For now, just does buy, no short TODO: make it calculate for short as well...
        float last_bought = 0;
        float last_sold = 0;

        for (int i = 0; i < total_data_size; i++){

            if (buy_sell[i] != null) { // making sure the data exists as it won't for the last index due to i+1 used previously in buy_sell
                if (buy_sell[i] == 1) { // BUY
                    currentHoldingPositionType = "buy";


                    float todayAveragePrice = (historicalData[i][5] + historicalData[i][1])/2; // average price of the day... 5 - close, 1 - open
                    last_bought = todayAveragePrice;
                    float gain = (last_bought-last_sold)/last_sold;
                    if (Double.isInfinite(gain) || Double.isNaN(gain)){
                        gain = 0; // as it's because divided by zero, which means no trades were done before so profit is zero.
                    }

                    total_gain = total_gain + gain;

                    if (log_trades) {
                        log = "SHORT-COVER/BUY,"+last_bought+ "," + gain*100 + "\n";
                        buy_sell_log.append(log);
                    }
                }

                if (buy_sell[i] == 0) { // SELL
                    currentHoldingPositionType = "sell";

                    if (last_bought != 0) { // if the stock has been bought once before...
                        // TODO: When would you sell? At the next day's open or at that day's close? - Make the decision
                        float todayAveragePrice = (historicalData[i][5] + historicalData[i][1])/2; // average price of the day... 5 - close, 1 - open
                        last_sold = todayAveragePrice;
                        float gain = (last_sold-last_bought)/last_bought;
                        if (Double.isInfinite(gain) || Double.isNaN(gain)){
                            gain = 0; // as it's because divided by zero, which means no trades were done before so profit is zero.
                        }

                        total_gain = total_gain + gain;
                        if (log_trades) {
                            log = "SELL/SHORT," + last_sold + "," + gain*100 + "\n";
                            buy_sell_log.append(log);
                        }
                    }
                } else {
                    currentHoldingPositionType = "hold";
                }
            }

            if (log_trades) {
                // CALCULATING DAILY UNREALIZED EQUITY VALUE
                if (i > 0) { // least one candle stick should pass by
                    float lastDayClose = historicalData[i - 1][5];
                    float todayClose = historicalData[i][5];
                    float difference = 0;

                    if (currentHoldingPositionType.equals("buy")) {
                        difference = (todayClose - lastDayClose) / todayClose;
                    }
                    if (currentHoldingPositionType.equals("sell")) {
                        difference = (lastDayClose - todayClose) / lastDayClose;
                    }

                    totalCloseUnrealisedProfit = totalCloseUnrealisedProfit + difference;
                    eachCloseProfit.append(totalCloseUnrealisedProfit + "\n");
                }
            }
        }

        if (log_trades) {
            fileHandler.writeToFile("data/stock/"+ticker+"/ma-crossover-trades-"+asset.historicalDataTimeframe+"-"+type1+"-"+ma1+"-"+type2+"-"+ma2+".csv", String.valueOf(buy_sell_log),false);
            fileHandler.writeToFile("data/stock/"+ticker+"/ma-crossover-trades-"+asset.historicalDataTimeframe+"-"+type1+"-"+ma1+"-"+type2+"-"+ma2+"-equitycurve.csv", String.valueOf(eachCloseProfit),false);
        }

        return new float[]{(total_gain * 100), numbers_of_trades};
    }

    public int[] simulate() throws Exception {
        int number_of_trades = 0;
        float[] result;

        float highest_returns = 0;
        int bestMA1 = 0;
        int bestMA2 = 0;
        int number_of_trades_best = 0; // number of trades for the best smas

        StringBuilder simulation_log = new StringBuilder();


        // starting with the lowest ma of 5 as lower numbers produce insanely high uncertainty
        // TODO: Figure out best best lower ma to start at
        for (int testMA1 = 12; testMA1<201; testMA1++){
            for (int testMA2 = 20; testMA2<201; testMA2++){
                if (testMA2 > testMA1) {
                    result = test(testMA1, testMA2, false);
                    float gain = result[0];
                    number_of_trades = (int) result[1];

                    // Comment this out to not log... TODO: Add a section in Preferences...
                    String log = type1 + "," + testMA1 + "," + type2 + "," + testMA2 + "," + gain + "," + number_of_trades + "\n";
                    simulation_log.append(log); // TODO: Include in Criterion - FAR FAR FAR more efficient then just String += log; less time and processing power

                    if (gain > highest_returns) {
                        highest_returns = gain;
                        bestMA1 = testMA1;
                        bestMA2 = testMA2;
                        number_of_trades_best = number_of_trades;
                    }
                }

            }
        }

        // TODO: This doesn't work currently, update it to the lastest trade it mades, currently if it does long,long,short, it says it shorted, ignoring the rest
        String position_type = "long";
        if(bestMA1>bestMA2){position_type = "short";}

//        System.out.println(stock.name + " | Best MA1 : " + bestMA1 + " & MA2 : " + bestMA2 + " | Returns : " + highest_returns + " | No. of Trades : " + number_of_trades + " | Type : " + position_type);
        String final_result = "MA-Crossover," + ticker + "," + type1 + "," + bestMA1 + "," + type2 + "," + bestMA2 + "," + highest_returns + "," + number_of_trades_best + "," + position_type;
        System.out.println(final_result);


        // Logging to a file...
        test(bestMA1,bestMA2,true); // making it log the trades...
        fileHandler.writeToFile("data/stock/"+ticker+"/simulation-"+type1+"-"+type2+"-"+asset.historicalDataTimeframe+".csv", simulation_log.toString(),false);
        fileHandler.writeToFile("data/simulation-result.csv",final_result,true); // adding all to a since file // TODO: remove duplicates

        // TODO: Want to return the highest returns as float? 30.73 % seems realistic and believable than 30 %
        return new int[]{bestMA1,bestMA2, (int) highest_returns, number_of_trades};
    }
}
