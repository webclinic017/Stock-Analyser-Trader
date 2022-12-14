package com.asset;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Forex extends Asset {

    // TODO: I don't think I wanna inherit this as the similarity are very limited and causes errors for normal api calls
    // TODO: https://tradermade.com/forex#pricing

    public Forex(String ticker) throws Exception {
        super(ticker);
        this.ticker = ticker;
        this.YFticker = ticker + "=X"; // separate ticker convention for YF...

        this.name = ticker;
        this.exchange = "FOREX";
        this.type = "forex";

        getHistorical_data();

        File local_icon = new File("data/stock/" + ticker + "/" + ticker + ".png");
        if (local_icon.exists()) {
            this.icon = new ImageIcon("data/stock/" + ticker + "/" + ticker + ".png"); // setting the icon to the local file if exists
        } else {
            this.icon = new ImageIcon(getLogo(ticker)); // setting the icon to the local file if exists
        }
    }


    @Override
    public Float[][] getHistorical_data() throws Exception {
        HistoricalData HistoricalDataGetter = new HistoricalData();
        this.historical_data = HistoricalDataGetter.get(ticker, YFticker, false, "1d", null);
        return historical_data;
    }

    public static String getLogo(String ticker) throws Exception {
        // getting the logo -- requesting my server to convert the file to png from svg - TODO: Using my server
        String url = "https://tools.lissankoirala.ml/svg-to-png?url=https://cloud.xm-cdn.com/static/research-portal/instruments_icons/" + ticker.toLowerCase() + ".svg";

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get("data/stock/" + ticker + "/" + ticker + ".png"));
        }
        return "data/stock/" + ticker + "/" + ticker + ".png";
    }
}
