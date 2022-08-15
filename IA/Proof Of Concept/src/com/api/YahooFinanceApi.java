package com.api;

import java.util.Date;

// TODO: try out features from https://yahoofinanceapi.com/
public class YahooFinanceApi {
    private String historical_data_url = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=%d&period2=%d&interval=1d&events=history&includeAdjustedClose=true";
    RequestHandler ReqHandler = new RequestHandler();
    Date now = new Date();

    public YahooFinanceApi(){
    }

    // not really a stock.com.api call but still acts like it...
    // Constructs an url to request
    public String get_historical(String ticker) throws Exception {
        long unixNow = now.getTime() / 1000L;
        // "-5364662325" - since the start
        // "1620777600" - 1y
        // "1589241600" - 2y
        String request_url = String.format(historical_data_url, ticker, 1589241600, unixNow); // url, ticker, start time, end time, time interval eg: 1d, 1M
        return ReqHandler.getString(request_url);
    }

    public String get_historical(String ticker, long start, long end) throws Exception {
        String request_url = String.format(historical_data_url, ticker, start, end); // url, ticker, start time, end time, time interval eg: 1d, 1M
        return ReqHandler.getString(request_url);
    }

}
