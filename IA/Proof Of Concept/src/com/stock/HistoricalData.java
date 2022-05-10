package com.stock;

import com.api.YahooFinanceApi;
import com.utils.FileHandler;
import com.utils.Utils;

public class HistoricalData {
    private YahooFinanceApi YFHandler = new YahooFinanceApi();
    private FileHandler fileHandler = new FileHandler();
    private Utils Utils = new Utils();


    public HistoricalData(){
    }


    public Float[][] get(String ticker) throws Exception {

        // changing the ticker format to YF; DOGEUSD to DOGE-USD
        ticker = ticker.replace(".", "-"); // eg : BRK.B - BRK-B
        ticker = ticker.replace("USD", "-USD");


        String filename = "data/ticker/" + ticker + ".csv";
        String historical_data = YFHandler.get_historical(ticker); // gets the data
        historical_data = historical_data.replace("Date,Open,High,Low,Close,Adj Close,Volume\n", ""); // cleaning up
        fileHandler.writeToFile(filename, historical_data, false); // storing it to make it easy to process, also to save having to ask everytime

//        System.out.println(FileHandler.getRowNumber(filename)); // this many data points, could look good in console...

        String[][] data = Utils.convertToMultiDArray(filename, 7); // 7 because it stays the same, 7 things in the column

        Float[][] final_data = Utils.convertStringArrayToFloatArray(data);

        // printing the array out
        // System.out.println(Arrays.deepToString(final_data));

        return final_data;

    }


}
