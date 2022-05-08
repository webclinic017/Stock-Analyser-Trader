package com.api;

import java.util.Date;

public class YahooFinanceApi {
    private String historical_data_url = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=%s&period2=%d&interval=%s&events=history&includeAdjustedClose=true";
    RequestHandler ReqHandler = new RequestHandler();
    Date now = new Date();

    public YahooFinanceApi(){
    }

    // not really a stock.com.api call but still acts like it...
    // Constructs an url to request
    public String get_historical(String ticker) throws Exception {
        long unixNow = now.getTime() / 1000L;
        // "-5364662325"
        String request_url = String.format(historical_data_url, ticker, "1620469437", unixNow, "1d"); // url, ticker, start time, end time, time interval eg: 1d, 1M
        return ReqHandler.getString(request_url);
    }

    public String get_historical(String ticker, int start, int end, String duration) throws Exception {
        String request_url = String.format(historical_data_url, ticker, start, end, duration); // url, ticker, start time, end time, time interval eg: 1d, 1M
        return ReqHandler.getString(request_url);
    }

}
