package com.analyzer.tools;

import com.asset.Asset;
import java.util.ArrayList;

// https://nullbeans.com/how-to-calculate-the-exponential-moving-average-ema/ TODO : DON'T MENTION THIS JUST THE MATHS, SAME WITH SMA
// TODO: refer to the maths behind this
public class EMA {
    private double period;
    ArrayList<Float> results = new ArrayList<>();

    public EMA(int period){
        this.period = period;
    }

    public ArrayList<Float> calculateEMA(Asset asset){
        ArrayList<Float> historicalData = asset.getHistorical_data(5);

        calculate(historicalData, period, historicalData.size()-1, results);
        return results;
    }

    private double calculate(ArrayList<Float> historicalData, double n, int i, ArrayList<Float> results){

        if(i == 0){
            results.add(historicalData.get(0)); // TODO: DOUBLE? FLOAT?
            return results.get(0);
        }else {
            double close = Double.parseDouble(String.valueOf(historicalData.get(i)));
            double factor = ( 2.0 / (n +1) );
            double ema =  close * factor + (1 - factor) * calculate(historicalData, n, i-1, results) ;
            results.add((float) ema); // TODO: round to 2 dec, real money value, can help show understanding
            return ema;
        }
    }

}