package com.analyzer;

// analyzes using all the relevant and makes a final decision

import com.analyzer.backtesting.CrossoverTester;
import com.api.AlpacaAPI;
import com.asset.Asset;
import com.google.gson.JsonArray;
import com.utils.FileHandler;
import com.utils.Math;

import java.util.ArrayList;

public class Analyze {
    private Math Math = new Math();
    AlpacaAPI AlpacaAPIHandler = new AlpacaAPI();
    FileHandler FileHandler = new FileHandler();

    // TODO : https://finnhub.io/docs/api/technical-indicator - add some of these indicators, hard to code all of them
    public Analyze(){
    }

    public void generateStocksBySector() throws Exception {
        JsonArray stocks = AlpacaAPIHandler.getStockList();
        FileHandler.writeToFile("data/stocks.txt",stocks.getAsString(),false);
        // TODO: Call the python program... say that python program makes a cache and requests the server periodically over time to prevent timeouts
    }

    // TODO: Make a hashmap of different sectors of stocks... using FinnhubAPI, perhaps just have a method to read it from a file and say it was compiled from python in my cloud server
    public ArrayList getStocksBySector(String sector) throws Exception {
        return null;
    }

    public String[] model(String[] tickers) throws Exception {
        for (String s: tickers){
            try {
                Asset asset = Asset.create(s); // gets the proper class for the asset, either stock, crypto or forex...
                CrossoverTester crossoverTester = new CrossoverTester(asset, "sma", "sma");

                new Thread(() -> {
                    try {
                        // This saves the results to their directory...
                        crossoverTester.simulate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }
        }

        // Analysis
        return null;

    }



}
