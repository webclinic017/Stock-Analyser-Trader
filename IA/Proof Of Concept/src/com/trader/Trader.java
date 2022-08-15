package com.trader;

import com.api.AlpacaAPI;
import com.asset.Asset;
import com.user.UserPreferences;

public class Trader {
    AlpacaAPI AlpacaAPIHandler = new AlpacaAPI();
    UserPreferences UserPreferences = new UserPreferences();

    public Trader(){
    }

    public void start(Asset asset, int sma1, int sma2, int timeframe, int amountPerTrade) throws InterruptedException {

        // Load user preferences... TODO: Probably make the user preference array global and initialised at startup

        while (true){
            // break after market close, check market-status every loop
            int timeTilNextCandle = 1;
            Thread.sleep(timeTilNextCandle);

            // get the price when the time hits, calculate the sma, if cross over, buy or sell...
            boolean crossover = false;
            if (crossover){
                asset.buy(amountPerTrade); // TODO: Converting this to money instead of trade size
            }
        }
    }
}
