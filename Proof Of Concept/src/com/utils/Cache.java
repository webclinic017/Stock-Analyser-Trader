package com.utils;

import com.api.RequestHandler;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// the time adding bit: https://mkyong.com/java/java-how-to-add-days-to-current-date/
public class Cache {

    public Cache() {
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        return dateFormat.format(c.getTime());
    }

    public static void resetExpiredURLs() {
        try {
            String executeCommand = "SELECT url FROM url_cache WHERE expire <= \""+getCurrentTime()+"\"";
            Connection connection = getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(executeCommand);
            ResultSet response = preparedStatement.executeQuery();

            ArrayList<String> expiredURLs = new ArrayList<>();

            while(response.next()) {
                expiredURLs.add(response.getString("url"));
            }

            // after getting the urls, deleting them from the cache...
            executeCommand = "DELETE FROM url_cache WHERE expire >= \""+getCurrentTime()+"\"";
            Statement statement = connection.createStatement();
            statement.execute(executeCommand);

            preparedStatement.close();
            connection.close();

            RequestHandler requestHandler = new RequestHandler();
            for(String url: expiredURLs){
                requestHandler.getString(url, true); // this method already has cache set to true so gets cached
            }


        } catch (Exception ignored) {
        }
    }

    private static String setExpireDate(String url){

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // convert calendar to date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // data which stays mostly the same but the financials can change overtime
        String[] oneDayExpireURLS = {"https://www.alphavantage.co/query?function=OVERVIEW", "https://finnhub.io/api/v1/stock/profile2"};

        // renewing the cache just in case the data changes like when a stock splits etc
        String[] oneMonthExpireURLS = {"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED", "https://query1.finance.yahoo.com/v7/finance/download", "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info"};

        for (String oneDayExpireURL : oneDayExpireURLS) {
            if (url.contains(oneDayExpireURL)) {
                c.add(Calendar.DATE, 1);
                Date currentDatePlusOneDay = c.getTime();
                return dateFormat.format(currentDatePlusOneDay);
            }
        }

        for (String oneMonthExpireURL : oneMonthExpireURLS) {
            if (url.contains(oneMonthExpireURL)) {
                c.add(Calendar.MONTH, 1);
                Date currentDatePlusOneMonth = c.getTime();
                return dateFormat.format(currentDatePlusOneMonth);
            }
        }

        // if url is not on expire list, still make it renew in 6 months time, just a good practise
        c.add(Calendar.MONTH, 6);
        Date currentDatePlusSixMonth = c.getTime();
        return dateFormat.format(currentDatePlusSixMonth);
    }

    private static Connection getDBConnection() throws SQLException {
        String databaseFile = "data/cache.db";
        Database DatabaseHandler = new Database(databaseFile);
        return DatabaseHandler.getConnection();
    }

    private static void createTable() throws SQLException {
        String createTableQuery = "" +
                "CREATE TABLE IF NOT EXISTS url_cache " +
                "(" +
                "url TEXT," +
                "data LONGTEXT," +
                "expire time" + // this so that it can hold lots of data as response can be in MBs
                ");";

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();
        statement.execute(createTableQuery);

        statement.close();
        connection.close();
    }

    public static void insert(String url, String data) {
        try {
            createTable();
            Connection connection = getDBConnection();

            String insertSQLCommand = "INSERT INTO url_cache(url, data, expire) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQLCommand);
            preparedStatement.setString(1, url);
            preparedStatement.setString(2, data);
            preparedStatement.setString(3, setExpireDate(url));

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();

        } catch (Exception e){
            System.out.println("Error SQLite insert, url: " + url);

        }
    }


    public static String get(String url) {
        try {
            createTable();
            String executeCommand = "SELECT * FROM url_cache WHERE url=?";
            Connection connection = getDBConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(executeCommand);
            preparedStatement.setString(1, url); // serializing

            String data = preparedStatement.executeQuery().getString("data");
            preparedStatement.close();
            connection.close();
            return data;

        } catch (Exception e){
            System.out.println("No cache found for: " + url);
            return null;
        }
    }
}
