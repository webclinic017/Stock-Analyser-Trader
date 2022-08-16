package com.trader;

import com.api.AlpacaAPI;
import com.asset.Asset;
import com.user.UserPreferences;
import com.utils.FileHandler;
import com.utils.Utils;


public class Trader {
    FileHandler FileHandler = new FileHandler();
    AlpacaAPI AlpacaAPIHandler = new AlpacaAPI();
    UserPreferences UserPreferences = new UserPreferences();

    public Trader(){
    }

    public void dayTimeFrameTrader(){
        String[][] assetList = Utils.convertToMultiDArrayFromCSV("DayTimeFrameList.csv", 3);
    }

    // TODO: Add a function if the timestep is 1d to check for crossover like 5 min before the market close or at market open
    public void start(Asset asset, int sma1, int sma2, String timeframe, int amountPerTrade) throws InterruptedException {

        if (timeframe.equals("1d")){
            // call another function... or rather put it in a file, with the ticker, sna1, sma2, then the function that will
            // run at market close will just check there...
            // TODO: Worth explaining this whole process in Criterion C : like as a last point to explain so everything else has been explained before
        }

        // Load user preferences... TODO: Probably make the user preference array global and initialised at startup

        while (true){
            // break after market close, check market-status every loop
            int timeTilNextCandle = 1;
            Thread.sleep(timeTilNextCandle);

            // checkNewCandle(); // Probably make python stream it on the set timeframe, then a java function checks for a change in a file to see if new data has come through...
            // calcSMA();
            // checkCrossover();

            // get the price when the time hits, calculate the sma, if cross over, buy or sell...
            boolean crossover = false;
            if (crossover){
                asset.buy(amountPerTrade); // TODO: Converting this to money instead of trade size
            }
        }
    }
}
