package com.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.utils.FileHandler;

import java.io.File;

// TODO: Retired api, not in use cause slow and inconsistent
public class AlphaVantageAPI {
    RequestHandler ReqHandler = new RequestHandler();
    FileHandler FileHandler = new FileHandler();

    private String api_key_token = "8DQF8MN8W4O10K8I";

    public AlphaVantageAPI(){
    }

    public JsonArray make_request(String request_url) throws Exception {
        return ReqHandler.get(request_url+"&apikey="+api_key_token);
    }

    public JsonArray company_profile(String ticker) throws Exception {
        String request_url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol="+ticker;
        return make_request(request_url);
    }

    public String get_historical(String ticker, String timeframe) throws Exception {
//        String request_url = String.format("", ticker, start, end, duration); // url, ticker, start time, end time, time interval eg: 1d, 1M

        String base_url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&apikey=8DQF8MN8W4O10K8I&symbol=" + ticker + "&interval="+timeframe;

        // TODO: worth mentioning criterion? deleting the contents of the file, need to do it becuase the following code appends to the file to prevent high memory usuage
        FileHandler.writeToFile("data/stock/" + ticker + "/" + ticker + "-intraday.csv", "", false);

        for (int i=1; i<=2; i++) {
            for (int j=1; j<=12; j++) {
                String request_url = base_url + "&slice=year" + i + "month" + j;
                // TODO: THE REPLACING PART DOESN'T WORK!
                String data = ReqHandler.getString(request_url).replace("\ntime,open,high,low,close,volume\n","");

                while (data.contains("Our standard API call frequency is 5 calls per minute")){
                    System.out.println("Sleeping cause hit limit...");
                    Thread.sleep(3000);
                    data = ReqHandler.getString(request_url).replace("\ntime,open,high,low,close,volume\n","");
                }

                FileHandler.writeToFile("data/stock/" + ticker + "/" + ticker + "-intraday.csv", data, true);
            }
        }



        return null;
//        return ReqHandler.getString(request_url);
    }

}
