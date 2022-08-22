package com.utils;

import java.sql.*;

public class Cache {
    String[] timeoutURLS = {"https://www.alphavantage.co/query?function=OVERVIEW", "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info", "https://finnhub.io/api/v1/stock/profile2"};

    public Cache() {
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
                "data LONGTEXT" + // this so that it can hold lots of data as response can be in MBs
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

            String insertSQLCommand = "INSERT INTO url_cache(url, data) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQLCommand);
            preparedStatement.setString(1, url);
            preparedStatement.setString(2, data);
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
