package com.utils;

public class Cache {
    Database DatabaseHandler = new Database("admin", "admin");

    public Cache(){
    }

    private String createSQLExecuteCommand(String data){
        return "INSERT INTO `urls` (`url`) VALUES ('" + data + "');"; // TODO: no no no, the single quote will be confused, there might be data that have quotation marks
    }

    public String get(String url){
        return null;
    }

    public void set(String url, String data, int timeout){
        // write to database
        try {
            DatabaseHandler.execute(createSQLExecuteCommand(data));
        } catch (Exception e){
            System.out.println("Saving into database failed");
            e.printStackTrace();
        }
    }

    public void set(String url, String data){
        set(url, data, -1); // TODO: maybe -1 won't work for SQL int
    }
}
