package com.asset;

import com.api.AlphaVantageAPI;
import com.api.YahooFinanceApi;
import com.utils.FileHandler;
import com.utils.Utils;

import java.text.ParseException;

public class HistoricalData {
    private YahooFinanceApi YFHandler = new YahooFinanceApi();
    private FileHandler fileHandler = new FileHandler();
    private AlphaVantageAPI AlphaVantageAPIHandler = new AlphaVantageAPI();
    private Utils Utils = new Utils();


    public HistoricalData(){
    }

    // timeframe Set to 1d if not using intraDay, if intraday: 1min, 5min, 15min, 30min, 60min
    // intraDay Only valid option for stocks

    public Float[][] get(String ticker, String YFticker, boolean intraDay, String timeframe, long[] startEnd) throws Exception {

        String filename = "";

        if (intraDay){ // If intraday value is true, the file would have already been downloaded for it...
            filename = AlphaVantageAPIHandler.getIntraDay(ticker, timeframe);

        } else { // if not intraday, download from YahooFinance
            filename = "data/stock/" + ticker + "/" + ticker + ".csv";

            String historical_data;
            if (startEnd != null){ // if start end provided, load that
                historical_data = YFHandler.get_historical(YFticker, startEnd[0], startEnd[1]);
            } else {
                historical_data = YFHandler.get_historical(YFticker);
            }

            historical_data = historical_data.replace("Date,Open,High,Low,Close,Adj Close,Volume\n", ""); // cleaning up

            fileHandler.writeToFile(filename, historical_data, false); // storing it to make it easy to process, also to save having to ask everytime
        }


//        System.out.println(FileHandler.getRowNumber(filename)); // this many data points, could look good in console...

        String[][] data = Utils.convertToMultiDArrayFromCSV(filename, 7); // 7 because it stays the same, 7 things in the column

        Float[][] final_data;

        try {
            final_data = convertStringArrayToFloatArray(data, timeframe);

        } catch (Exception e){ // catching points where this might be null in the data, ONLY executes if catches null value errors
            System.out.println("Null value found in the historical data");
            e.printStackTrace();

            // remove any lines with null data points -- repeat from Utils because of some changes...
            final_data = new Float[data.length][data[0].length]; // figuring out how big the original array was

            // can't make it zero... // TODO: Include this in Criterion.
            // this to replace any null values for close data, as it's the closet to replacing the missing values
            float[] previous_day_data = new float[data[0].length];

            for (int x = 0; x < data.length; x++){
                for (int y = 0; y < data[x].length; y++){

                    if (y == 0) { // if it's a date, convert to unix timestamp...
                        final_data[x][y] = Utils.getUnix(data[x][y], timeframe);
                    } else {
                        String value = data[x][y];
                        if (value == null){
                            value = "null";
                            System.out.println("Converted to null");
                        }
                        if (value.equals("null")) { // replacing null with previous day's data, this is a temporary approach as that data will eventually be replaced by the data provider
                            // substituting the value with previous day data
                            final_data[x][y] = previous_day_data[y];
                        }

                        else {
                            final_data[x][y] = Float.parseFloat(data[x][y]);
                            // updating previous day data
                            previous_day_data[y] = final_data[x][y];

                        }
                    }
                }
            }
        }

        // printing the array out
        // System.out.println(Arrays.deepToString(final_data));

        return final_data;

    }

    public Float[][] get(String ticker) throws Exception {
        return get(ticker, ticker, false, "1d", null); // calling the function and passing the same parameters as if YFticker is not given, assumes it's the same
    }


    // TODO: https://finnhub.io/docs/api/stock-candles | https://finnhub.io/docs/api/crypto-candles - useful for smaller resolution, intra-day trades...


    private Float[][] convertStringArrayToFloatArray(String[][] array, String timeframe) throws ParseException { // returns a float arraylist

        Float[][] floatArray = new Float[array.length][array[0].length]; // figuring out how big the original array was

        for (int x = 0; x < array.length; x++){
            for (int y = 0; y < array[x].length; y++){
                if (y == 0) { // converting date to unix timestamp...
                    floatArray[x][y] = Utils.getUnix(array[x][y], timeframe);
                } else {
                    floatArray[x][y] = new Float(array[x][y]);
                }
            }
        }

        return floatArray;
    }

}
