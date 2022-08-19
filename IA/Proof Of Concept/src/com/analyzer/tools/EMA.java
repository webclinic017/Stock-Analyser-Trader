package com.analyzer.tools;

import com.asset.Asset;
import java.util.ArrayList;

// https://nullbeans.com/how-to-calculate-the-exponential-moving-average-ema/ TODO : DON'T MENTION THIS JUST THE MATHS, SAME WITH SMA
public class EMA {

    public static double[] calculateEMA(Asset asset, double n){
        ArrayList<Float> historicalData = asset.getHistorical_data(5);
        double[] results = new double[historicalData.size()];

        calculate(historicalData, n, historicalData.size()-1, results);
        return results;
    }

    private static double calculate(ArrayList<Float> historicalData, double n, int i, double[] results){

        if(i == 0){
            results[0] = Double.parseDouble(String.valueOf(historicalData.get(0))); // TODO: DOUBLE? FLOAT?
            return results[0];
        }else {
            double close = Double.parseDouble(String.valueOf(historicalData.get(i)));
            double factor = ( 2.0 / (n +1) );
            double ema =  close * factor + (1 - factor) * calculate(historicalData, n, i-1, results) ;
            results[i] = ema;
            return ema;
        }
    }

}