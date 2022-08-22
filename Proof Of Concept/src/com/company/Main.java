package com.company;

import com.analyzer.Analyze;
import com.analyzer.tools.EMA;
import com.api.AlpacaAPI;
import com.api.AlphaVantageAPI;
import com.api.FinnhubAPI;
import com.api.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gui.GUICaller;
import com.asset.Asset;
import com.trader.Trader;
import com.utils.Cache;
import com.utils.Database;
import com.utils.FileHandler;

import java.util.Arrays;

// TODO: FIND A WAY TO SEPARATE THE DATA INTO TRAINING AND TESTING - INCLUDE IN CRITERIONS AND EMAIL COMMUNICATIONS...

public class Main {

    public static void main(String[] args) throws Exception {

//        YahooFinanceApi YFHandler = new YahooFinanceApi();
//        String historical_data = YFHandler.get_historical("DOGE-USD");
//        System.out.println(historical_data);
//
//        FileHandler FileHandler = new FileHandler();
//        FileHandler.writeToFile("TSLA.csv", historical_data, false);


        AlpacaAPI AlpacaAPIHandler = new AlpacaAPI();
        FinnhubAPI FinnhubAPIHandler = new FinnhubAPI();
        FileHandler FileHandler = new FileHandler();

//        System.out.println(AlpacaAPIHandler.clock());
//        System.out.println(AlpacaAPIHandler.account());
//        System.out.println(AlpacaAPIHandler.orders());
//        System.out.println(AlpacaAPIHandler.positions());
//        System.out.println(AlpacaAPIHandler.watchlists());
//
//
//        // Gets all the assets
//        //System.out.println(AlpacaAPIHandler.assets());
//        System.out.println(AlpacaAPIHandler.ticker_info("TSLA"));
//        System.out.println(FinnhubAPIHandler.company_profile("TSLA"));


//
//        System.out.println(AlpacaAPIHandler.portfolio_history());
//        //System.out.println(AlpacaAPIHandler.stock_trades("TSLA"));
//        System.out.println(AlpacaAPIHandler.get_news());
//        System.out.println(AlpacaAPIHandler.get_news("DOGEUSD"));
//
//        Math Math = new Math();
//        ArrayList<Integer> numbs = new ArrayList<>();
//
//        for(int i = 0; i<1000; i++){
//            numbs.add(i);
//        }
//
//        float average = Math.average(numbs);
//        System.out.println(average);

//        Asset stock = Asset.create("AAPL");
//        System.out.println(stock.info);
//        System.out.println(stock.getNewsData());

//        System.out.println(Arrays.deepToString(stock.historical_data));

//        Analyze Analyzer = new Analyze();

//        SMACrossover smaCrossover = new SMACrossover();
//        System.out.println(smaCrossover.check(stock, 50, 180)); // returns true or false, over or under.

        GUICaller GUICaller = new GUICaller();
//        GUICaller.Login();
        GUICaller.startup();
//        GUICaller.HomeScreen();
//        GUICaller.AssetInfo(stock);
//        GUICaller.SimulationResults(stock);

//        JsonArray data = AlpacaAPIHandler.getStockList();
//        String value = String.valueOf(data);
//        FileHandler.writeToFile("stock.csv", value, false);
//
//        data = AlpacaAPIHandler.getStockList();
//        value = String.valueOf(data);
//        FileHandler.writeToFile("crypto.csv", value, false);


//        for(int i = 0; i < 5; i++) {
//            MultiThreadRunner multiThreadRunner = new MultiThreadRunner();
//            multiThreadRunner.start();
//        }

//        System.out.println(AlpacaAPIHandler.ticker_info("TSLA"));

//        Stock stock = new Stock("AAPL");
//        Crypto crypto = new Crypto("ETHUSD");
//        Forex forex = new Forex("GBPUSD");

//        GUICaller.Simulate(crypto);


//        System.out.println(stock.name);
//        System.out.println(stock.getNewsData());

//        SMACrossoverTester smaCrossoverTester = new SMACrossoverTester(forex);

        // run a simulation to figure out which one smas would have done the best for this stock...
//        System.out.println("\n\nSMA1 : 61 & SMA2 : 39 - " + Arrays.toString(smaCrossoverTester.test(51, 39, true)));

//        System.out.println("\n\nRunning simulation to figure out the best SMAs");
//        smaCrossoverTester.simulate();

        // Simulating for a array of stocks...
//        String[] stocks = {"BTCUSD","ETHUSD","AAPL","MSFT","AMZN","TSLA","GOOG","FB","NVDA","JPM","V","MA","BAC","DIS","MCD","NFLX","BLK","BA"};
//        for (String s: stocks){
//            Asset asset = Asset.create(s); // gets the proper class for the asset, either stock, crypto or forex...
//            SMACrossoverTester smaCrossoverTester1 = new SMACrossoverTester(asset);
//
//            new Thread(() -> {
//                try {
//                    smaCrossoverTester1.simulate();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }

//        Watchlist watchlists = new Watchlist("default");
//        String[] watchlist = watchlists.get();


//        watchlist.addTicker("AAPL");
//        watchlist.addTicker("TSLA");
//        watchlist.addTicker("BTCUSD");
//        watchlist.remove("TSLA");
//        System.out.println(Arrays.toString(watchlist.get()));


        // TODO: Add dates to the trades log... [for backtesting log]

//        Correlation Correlation = new Correlation();
//        Correlation.find("GBTC,BTC-USD", "2020-01-01", "2022-06-01");

//        HomeScreen.searchTickerOrName("");

//        Asset crypto = Asset.create("BTCUSD");
//        System.out.println(crypto.price());
//        System.out.println(crypto.quote());

//        System.out.println(AlpacaAPIHandler.portfolioHistory()); // TODO: Acts weired but will get fixed soon as they say
        // TODO: Extract the equity part of it and graph that... with losses as red and profits as green and with a start equity line

//        System.out.println(AlpacaAPIHandler.createWatchlist("main", "TSLA,AAPL,MSFT"));
//        System.out.println(AlpacaAPIHandler.watchlist("main"));

        // TODO: GET ALL THE AVAILABLE STOCKS FROM ALPACA, THEN SEPARATE THEM INTO CATEGORIES USING FINNHUB API, THEN SIMULATE THEM... AFTER THAT FIND A MEAN TO FIT ALL THE STOCKS FROM THAT CATEGORY... PERHAPS USE PYTHON TO DO AND USE SOME DATA SCIENCE STUFF...

//        System.out.println(AlpacaAPIHandler.getStockList());
//        System.out.println(Asset.industry("TSLA"));

//        System.out.println(FinnhubAPIHandler.constituents("NDX"));

        Analyze Analyzer = new Analyze();
//        Analyzer.model(Asset.constituents("NDX"));

//        System.out.println(AlpacaAPIHandler.executeOrder(stock, 1, "buy"));

        // TODO: Alpha Vantage API key: 8DQF8MN8W4O10K8I
        // TODO: DEFINITELY IMPLEMENT THIS (BUT PERHAPS STICK WITH 1D SIMULATION OR EVEN WHEN CREATING AN ACCOUNT, HAVE AN TYPE LIKE AGGRESSIVE, PASSIVE ETC)
        // TODO: This has intraday option, show client interaction, possibly after the client tested and said he had to wait days for a hit so he wanted me to train intraday stocks so i needed intraday market data for free
        // TODO: https://www.alphavantage.co/documentation/#intraday-extended
        // TODO: Figure out adjusted close difference and either add to one data set or remove from another

//        System.out.println(Arrays.deepToString(stock.getIntraDay("60min")));
//        System.out.println(Arrays.deepToString(stock.getHistorical_data()));

        // TODO: Enlarge the graph in the SimulationGUI
        // TODO: Speed up the the .simulate() by using proper string manipulation

        Trader Trader = new Trader();
//        Trader.dayTimeFrameTrader();
//        System.out.println(Trader.timeTilNextCandle("60min"));

//        System.out.println(Asset.getLogo("MU"));
//        GUICaller.CustomizeSimluation(stock);

        // TODO: MAKE THE SIMULATION ACTUALLY SHOW RESULTS AT THE END LIKE SOME EXAMPLES OR PROFIT GRAPHS OVER TIME... !!! IMPORTANT !!!

        // TODO: REMOVE THE RESTRICTION OF THE SMA TESTING TO LESS, SHOW IN CLIENT INTERACTION HE WANTED MORE TRADES HAPPENING... AND AFTER CHANGING IT MADE HIM MORE PROFITS AND IT WAS MORE RELIABLE
        // TODO: CHOOSE THE 5 SAMPLE SIMULATIONS SHOWN WISELY, MAYBE ONE FOR EACH CATEGORY ETC... OR MAKE IT ACCORDING TO THE USER PREFERENCE, THEN CAN SHOW IN SUCCESS CRITERION

//        EMA ema = new EMA(20);
//        System.out.println(ema.calculateEMA(stock));

        // TODO: MIGHT HAVE TON OF BUGS TO BE FIXED
        // TODO: SPECIALLY WHEN THE FILE IS SAVED AND THE REREAD, THE LOG FILE.
        // TODO: THIS WORK IS FOR TOMORROW TO BE FIXED.
        //
        // TODO: REDUCE THE LIMIT TO WHICH MAs CAN BE CHOSEN

        // TODO: SORT THE MOVING AVERAGES AS A CATEGORY, THEN DECIDE WHICH TYPE TO USE, LET'S SAY IF THE DX/DY IS SMALL, THEN WHAT? ETC
        // TODO: THEN DO THESE CALCULATIONS TO FIND OUT LIKE HOW MUCH LEVERAGE TO USE, ETC. READ MY FIRST NOTES ON THIS PROJECT
        // TODO: ALSO, TRADE MULTIPLE MARKETS TO INCREASE THE ODDS OF CAPTURING TRENDS...


        // TODO: HAVE A WAY TO SEEING EXISTING POSITION BRFORE BUYING SELLING

        // TODO: THEN HAVE A WAY TO CALCULATE THE STOP LOSS



        // DATABASE
//        Database DatabaseHandler = new Database("data/cache.db");
//        Cache cache = new Cache();
//        cache.createTable();
//        Cache.insert("https://google.com", "this is the test data, with ' too");
//        System.out.println(Cache.get("https://google.com"));
//        Cache.insert("https://google.com", "this is the test data, with ' too");
//        System.out.println(Cache.get("https://google.com"));



        // TODO: clear api cache from finnhub and alphavantage, run a update thread on background, once every x, just for company profile
    }
}
