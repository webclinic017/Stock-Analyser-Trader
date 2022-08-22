package com.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.utils.Cache;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class RequestHandler {

    public RequestHandler(){
    }

    public String tryCache(String url){
        return Cache.get(url);
    }

    public void setCache(String url, String data){
        Cache.insert(url, data);
    }

    // Used JsonArray to meke all use the same type, cause the stock.com.api sometimes returns JsonObject and sometimes JsonArray
    // So this, converts the JsonObject to JsonArray
    public JsonArray jsonify(String data){
        try {
            JsonArray jsondata = new Gson().fromJson(data, JsonArray.class);
            return jsondata;

        } catch (Exception e){
            data = "["+data+"]";
            JsonArray jsondata = new Gson().fromJson(data, JsonArray.class);
            return jsondata;
        }
    }

    public String getString(String url, boolean cache) throws Exception {
        // Checking if cache exists
        if (cache){
            String cacheResponse = tryCache(url);
            if (cacheResponse!=null) {return cacheResponse;}
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String data = response.body();

        if (cache){Cache.insert(url, data);} // Saving to cache

        return data;
    }

    public JsonArray get(String url, boolean cache) throws Exception {
        // Checking if cache exists
        if (cache){
            String cacheResponse = tryCache(url);
            if (cacheResponse!=null) {return jsonify(cacheResponse);}
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String data = response.body();

        if (cache){Cache.insert(url, data);} // Saving to cache

        return jsonify(data);
    }

    // TODO : make the numbers of cookies dynamic, pass it as a array or something!
    public JsonArray get(String url, String header_name_1, String header_value_1, String header_name_2, String header_value_2, boolean cache) throws Exception {
        // Checking if cache exists
        if (cache){
            String cacheResponse = tryCache(url);
            if (cacheResponse!=null) {return jsonify(cacheResponse);}
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(header_name_1, header_value_1)
                .header(header_name_2, header_value_2)
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String data = response.body();

        if (cache){Cache.insert(url, data);} // Saving to cache

        return jsonify(data);
    }



    // TODO: WATCH THIS VIDEO!!! - https://www.youtube.com/watch?v=7H0sqS-ZJw0
    public String post(String uri, String header_name_1, String header_value_1, String header_name_2, String header_value_2, String post_data) throws Exception {

        URL url = new URL(uri);
        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        //adding header
        httpURLConnection.setRequestProperty(header_name_1,header_value_1);
        httpURLConnection.setRequestProperty(header_name_2, header_value_2);
        httpURLConnection.setDoOutput(true);

        //Adding Post Data
        OutputStream outputStream=httpURLConnection.getOutputStream();
        outputStream.write(post_data.getBytes());
        outputStream.flush();
        outputStream.close();

        String line="";
        InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        StringBuilder response=new StringBuilder();
        while ((line=bufferedReader.readLine())!=null){
            response.append(line);
        }
        bufferedReader.close();

        return response.toString();
    }
}