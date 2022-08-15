package com.asset;

import com.api.AlphaVantageAPI;
import com.api.FinnhubAPI;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://companiesmarketcap.com/
public class Stock extends Asset {
    FinnhubAPI FinnhubAPIHandler = new FinnhubAPI();
    AlphaVantageAPI AlphaVantageAPIHandler = new AlphaVantageAPI();

    private String YFticker;


    public Stock(String ticker) throws Exception {
        super(ticker);
        this.YFticker = ticker.replace(".", "-"); // convention ticker for YF...
        getHistorical_data();

        // adding basic info to the stock



        // checking if local file for icon exists // TODO: add this to criterion for caching complexity
        if (!local_icon.exists()) { // if file doesn't exists// setting the icon to the local file if exists

            String url = "https://companiesmarketcap.com/img/company-logos/128/" + YFticker + ".png";

            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get("data/stock/" + ticker + "/" + ticker + ".png"));
            } catch (Exception e){
                System.out.println("Icon error : " + e);
            }

            this.icon = new ImageIcon("data/stock/" + ticker + "/" + ticker + ".png"); // setting the icon to the local file if exists
        }

        // adding more information to the stock
        // TODO: this does add a lot of delay, so might want to save this info to a file so don't have to do the request next time too...
        // TODO: not using it until done so...
        try {
            JsonObject other_data = FinnhubAPIHandler.company_profile(ticker).get(0).getAsJsonObject();

            this.name = other_data.get("name").getAsString();
            this.country = other_data.get("country").getAsString();
            this.industry = other_data.get("finnhubIndustry").getAsString();
            this.ipo = other_data.get("ipo").getAsString(); // TODO: Add ipo data in simulation gui, helpful to choose dates
            this.weburl = other_data.get("weburl").getAsString();

        } catch (Exception e) {
            System.out.println("Stock info not found in Finnhub...");
        }

        try {
            JsonObject other_data = AlphaVantageAPIHandler.company_profile(ticker).get(0).getAsJsonObject();
            System.out.println(other_data);

            this.sector = other_data.get("sector").getAsString();
            this.exchange = other_data.get("exchange").getAsString();
            this.marketcap = other_data.get("MarketCapitalization").getAsString();
            this.PERatio = other_data.get("PERatio").getAsFloat();
            this.EPS = other_data.get("EPS").getAsFloat();
            this.analystTarget = other_data.get("AnalystTargetPrice").getAsFloat();
            this.PriceToSalesRatio = other_data.get("PriceToSalesRatioTTM").getAsFloat();
            this.yearHigh = other_data.get("52WeekHigh").getAsFloat();
            this.yearLow = other_data.get("52WeekLow").getAsFloat();


        } catch (Exception e) {
            System.out.println("Stock info not found in AlphaVantage...");
        }



    }


}
