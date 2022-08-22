package com.utils;

import java.sql.*;

public class Database {
    String username; // TODO: Get this from user preference file
    String password;

    public Database(String username, String password){
        this.username = username;
        this.password = password;
    }

    public ResultSet execute(String query) throws SQLException { // we want to throw exception if it doesn't work, so it can do the normal request
        
        // Creating a connection to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock", username , password);

        // Creating a statement
        Statement statement = connection.createStatement();

        // 3. Execute SQL query
        ResultSet response = statement.executeQuery(query);

        return response;
        // TODO: close the connection

    }

}
