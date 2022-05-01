package com.analyzer.strategies;

import com.analyzer.Analyze;
import com.stock.Stock;

import java.util.ArrayList;

public class SMACrossover {
    public SMACrossover(){
    }

    // checks if the stock's sma1 is over or under sma2, returns true or false
    public boolean check(Stock stock, int sma1, int sma2) throws Exception {
        Analyze Analyzer = new Analyze();
        ArrayList<Float> sma_data = Analyzer.sma(stock, sma1);
        Float sma_1 = sma_data.get(sma_data.size()-1);

        ArrayList<Float> sma_data_2 = Analyzer.sma(stock, sma2);
        Float sma_2 = sma_data_2.get(sma_data_2.size()-1);

        System.out.println(sma_1);
        System.out.println(sma_2);

        return sma_1 > sma_2; // true or false
    }
}
