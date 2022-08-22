package com.utils;

import java.sql.*;

// TODO: when reading and writing add a few tries before returning error as when writing the db is locked
public class Database {
    String databaseFile;

    public Database(String databaseFile){
        this.databaseFile = databaseFile;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFile);
        return connection;
    }

    public ResultSet execute(String query) {

        try {
            // Creating a connection to the database
            Connection connection = getConnection();

            // Creating a statement
            Statement statement = connection.createStatement();

            // Executing SQL query
            ResultSet response = statement.executeQuery(query);

            // Closing connection to the database
            connection.close();

            return response;

        } catch (Exception e){
            System.out.println("Error executing SQLite Query");
            e.printStackTrace();
            return null;
        }
    }

}
