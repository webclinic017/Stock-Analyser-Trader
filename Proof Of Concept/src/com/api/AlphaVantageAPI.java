package com.api;

import com.google.gson.JsonArray;
import com.utils.FileHandler;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

// TODO: Retired api, not in use cause slow and inconsistent
public class AlphaVantageAPI {
    RequestHandler ReqHandler = new RequestHandler();
    FileHandler FileHandler = new FileHandler();

    private String api_key_token = "8DQF8MN8W4O10K8I";

    public AlphaVantageAPI(){
    }

    public JsonArray make_request(String request_url, boolean cache) throws Exception {
        return ReqHandler.get(request_url+"&apikey="+api_key_token, cache);
    }

    public JsonArray company_profile(String ticker) throws Exception {
        String request_url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol="+ticker;
        return make_request(request_url, true);
    }

    public String getIntraDay(String ticker, String timeframe) throws Exception {
//        String request_url = String.format("", ticker, start, end, duration); // url, ticker, start time, end time, time interval eg: 1d, 1M
        String base_url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&apikey=8DQF8MN8W4O10K8I&symbol=" + ticker + "&interval="+timeframe;

        // TODO: worth mentioning criterion? deleting the contents of the file, need to do it becuase the following code appends to the file to prevent high memory usuage
        FileHandler.writeToFile("data/stock/" + ticker + "/" + ticker + "-" + timeframe.toLowerCase() + ".csv", "", false);

        for (int i=1; i<=2; i++) {
            for (int j=1; j<=12; j++) {
                String slice = "&slice=year" + i + "month" + j;
                String request_url = base_url + slice;

                // as it's the current month, we want updated data every request
                boolean cache = !slice.equals("&slice=year1month1");

                String data = ReqHandler.getString(request_url, cache);
                while (true){
                    if (data.contains("Our standard API call frequency is 5 calls per minute")) {
                        System.out.println("Sleeping cause hit API limit...");
                        Thread.sleep(3000);
                        data = ReqHandler.getString(request_url, cache);
                    }
                    else {
                        break;
                    }
                }

                data = data.replaceAll("time,open,high,low,close,volume", "").replaceAll("(?m)^[ \t]*\r?\n", "");
                FileHandler.writeToFile("data/stock/" + ticker + "/" + ticker + "-" + timeframe.toLowerCase() + ".csv", data, true);
            }
        }

        ArrayList<String> data = FileHandler.readFromFile("data/stock/" + ticker + "/" + ticker + "-" + timeframe.toLowerCase() + ".csv");

        // REVERSING THE ENTIRE FILE AS YEAR2MONTH12 IS THE EARLIEST DATA, AND WE WANT THAT TO BE IN THE TOP OF THE FILE FOR SIMULATION
        data = Utils.reverseArrayList(data);


        StringBuilder finalString = new StringBuilder();

        for (int i = 0; i<data.size(); i++){
            // Need to add an Adjusted Close field on the dataset as how YahooFinance API has in it's dataset
            String[] newString = data.get(i).split(",");

            StringBuilder changedString = new StringBuilder(); // more efficient
            for(int j=0; j< newString.length; j++){
                if (j==4){ // adding an extra on to duplicate the Close to Adj Close
                    changedString.append(newString[j]).append(",");
                }
                changedString.append(newString[j]);
                if (j != newString.length-1){
                    changedString.append(",");
                }
            }

            finalString.append(changedString).append("\n");
        }

        // TODO: MAIN PRIORITY, MAKE SURE NEW LINE CHARACTERS ARE REMOVED... THE REMOVE THE CHANGE OF NULL TO STRING IN HISTORICALDATA.JAVA

        FileHandler.writeToFile("data/stock/" + ticker + "/" + ticker + "-" + timeframe.toLowerCase() + ".csv", String.valueOf(finalString), false);
        return "data/stock/" + ticker + "/" + ticker + "-" + timeframe.toLowerCase() + ".csv"; // returns the file location...
    }

}
