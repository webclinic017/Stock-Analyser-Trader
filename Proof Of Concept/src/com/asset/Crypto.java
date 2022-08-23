package com.asset;

import com.api.CoinMarketCapAPI;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Crypto extends Asset {

    private String Baseticker;
    static CoinMarketCapAPI CoinMarketCapAPIHandler = new CoinMarketCapAPI();

    public Crypto(String ticker) throws Exception {
        super(ticker);
        this.ticker = ticker;
        this.YFticker = ticker.replace("USD", "-USD"); // convention ticker for YF...
        this.Baseticker = ticker.replace("USD", "");
        getHistorical_data();

        // setting some info about it
        JsonObject response = CoinMarketCapAPIHandler.crypto_info(Baseticker).get(0).getAsJsonObject();
        System.out.println(response);
        JsonObject data = response.get("data").getAsJsonObject();
        data = data.get(Baseticker).getAsJsonObject();

        this.name = name.replace(" pair", ""); // BTC/USD instead of BTC/USD pair
        this.country = "Global";
        this.industry = "Blockchain";
        this.sector = "Crypto";
        this.ipo = data.get("date_added").getAsString().split("T")[0];
        this.weburl = data.get("urls").getAsJsonObject().get("explorer").getAsJsonArray().get(0).getAsString();
        // TODO: maybe show weburl

        // getting the right logo...


        // checking if local file for icon exists // TODO: add this to criterion for caching complexity
        File local_icon = new File("data/stock/" + ticker + "/" + ticker + ".png");
        if (local_icon.exists()) {
            this.icon = new ImageIcon("data/stock/" + ticker + "/" + ticker + ".png"); // setting the icon to the local file if exists
        } else {
            this.icon = new ImageIcon(getLogo(ticker)); // setting the icon to the local file if exists
        }
    }

    @Override
    public Float[][] getHistorical_data() throws Exception {
        this.historical_data = HistoricalDataGetter.get(ticker, YFticker, false, "1d", null);
        return historical_data;
    }

    @Override
    public String quote() throws Exception {
        JsonObject response = AlpacaAPIHandler.quoteCrypto(ticker).get(0).getAsJsonObject();
        System.out.println(response);
        String quote = response.get("quote").getAsJsonObject().get("ap").getAsString();
        return quote;
    }

    @Override
    public String price() throws Exception {
        JsonObject response = AlpacaAPIHandler.latestTradeCrypto(ticker).get(0).getAsJsonObject();
        System.out.println(response);
        String quote = response.get("trade").getAsJsonObject().get("p").getAsString();
        return quote;
    }


    public static String getLogo(String ticker) throws Exception {
        String Bticker = ticker.replace("USD", "");
        JsonObject response = CoinMarketCapAPIHandler.crypto_info(Bticker).get(0).getAsJsonObject();
        System.out.println(response);
        JsonObject data = response.get("data").getAsJsonObject();
        data = data.get(Bticker).getAsJsonObject();

        String url = data.get("logo").getAsString().replace("64x64","128x128"); // asking for a higher quality image...
        String ipo = data.get("date_added").getAsString();

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get("data/stock/" + ticker + "/" + ticker + ".png"));
        }

        return "data/stock/" + ticker + "/" + ticker + ".png"; // returns the location of the file...
    }
}
