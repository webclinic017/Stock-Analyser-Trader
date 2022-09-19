package com.utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

public class Utils {
    public Utils(){
    }

    // convert to multi Dimension array from a CSV
    public static String[][] convertToMultiDArrayFromCSV(String fileName, int column){ // only needs columns cause can find row with preious methods
        FileHandler FileHandler = new FileHandler();

        int row = 0;
        String[][] array = new String[FileHandler.getRowNumber(fileName)][column];

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while((line=bufferedReader.readLine()) != null){
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                while(tokenizer.hasMoreTokens()){
                    for(int col = 0; col<column; col++){
                        array[row][col] = tokenizer.nextToken();
                    }
                }
                row++;
            }

        } catch (Exception e){
            System.out.println("While converting");
            System.out.println(e);
        }

        return array;
    }


    public float getUnix(String dateString, String timeframe){
        try {
            long unixTime = 0;

            if (timeframe.equals("1d")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateString);
                unixTime = date.getTime() / 1000;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = dateFormat.parse(dateString);
                unixTime = date.getTime() / 1000;
            }
            return (float) unixTime;

        } catch (Exception ignored){
            return (float) 0;
        }
    }

    public static String unixToDate(float unix_time, String timeframe){
        Date date = new Date();
        date.setTime((long) unix_time*1000);

        if (timeframe.equals("1d")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            return sdf.format(date);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd MMM yyyy");
        return sdf.format(date);
    }

    // Bubble sort to sort either in ascending or descending
    public int[] sortArray(int[] array, boolean ascending){
        boolean swapped = true;
        int sorted_count = 0;

        while(swapped){
            swapped = false; // set the initial value to false as if it doesn't change to true, nothing has been saved.

            for(int i=0; i<array.length-1-sorted_count; i++){
                int a = array[i];
                int b = array[i+1];

                // Sorting in ascending order or descending order
                if(ascending){ // If ascending is true, make sure 1st is smaller then 2nd
                    if(a > b){
                        array[i] = b;
                        array[i+1] = a;
                        swapped = true;
                    }
                } else{ // This is if the ascending, so makes sure the 1st number is bigger than the 2nd number
                    if(a < b){
                        array[i] = b;
                        array[i+1] = a;
                        swapped = true;
                    }
                }
            }
            sorted_count++;
        }
        return array;
    }


    public static Float[] getRowOf2DArray(String[][] array, int rowNum){
        Float[] toReturn = new Float[array.length];
        for (int i = 0; i < array.length; i++){
            try {
                toReturn[i] = Float.valueOf(array[i][rowNum]);
            } catch (Exception ignored){} // as it simply means it reached a empty line
        }
        return toReturn;
    }


    // returns in acending order
    public static ArrayList<String> selectionSortByColumn(ArrayList<String> arrayList, String[][] array, int column) {

        Float[] arraySortRow = getRowOf2DArray(array, column);

        int arrayLength = arraySortRow.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < arrayLength-1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < arrayLength; j++) {
                try {
                    if (arraySortRow[j] < arraySortRow[min_idx]) {
                        min_idx = j;
                    }
                } catch (Exception e){
                }
            }

            // Swap the found minimum element with the first element
            try {
                float temp = arraySortRow[min_idx];
                arraySortRow[min_idx] = arraySortRow[i];
                arraySortRow[i] = temp;

                // SWAPPING THE ACTUAL ARRAYLIST
                String tempA = arrayList.get(min_idx);
                arrayList.set(min_idx, arrayList.get(i));
                arrayList.set(i, tempA);
            } catch (Exception ignored){ // some lines are empty and will hit null exception
            }

        }
        arrayList = reverseArrayList(arrayList);

        return arrayList;
    }

    public static float[] findHighestAndLowest(float[] data){
        float highest = data[0];
        float lowest = data[0]; // make it the first number of the element can't be zero numbers might always be bigger than zero,
        // TODO: lead to getting divide by zero... by putting default value as 0
        for(float num: data){
            if (num > highest){
                highest = num;
            }
            else if (num < lowest){
                lowest = num;
            }
        }

        return new float[]{highest, lowest};
    }

    public static float[] findHighestAndLowest(ArrayList<Float> data){
        float[] float_data = new float[data.size()];
        for (int i = 0; i<data.size(); i++){
            float_data[i] = data.get(i);
        }
        return findHighestAndLowest(float_data);
    }

    // TODO: provide the link from where i got it
    public static ArrayList<String> reverseArrayList(ArrayList<String> arrayList) {
        ArrayList<String> revArrayList = new ArrayList<>();
        for (int i = arrayList.size() - 1; i >= 0; i--) {
            // Append the elements in reverse order
            revArrayList.add(arrayList.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }

    // data, int index_to_stop, boolean from front or from start...
    public static ArrayList<Float> stripArrayList(ArrayList<Float> data, int index_to_stop, boolean from_front){
        ArrayList<Float> newarraylist = new ArrayList<>();

        if (from_front){
            for (int i = 0; i<index_to_stop; i++){
                newarraylist.add(data.get(i));
            }

        } else { // if strip from back
            // TODO: might want to use Stack, last in first out, for complexity

            int counter = data.size() - index_to_stop;
            for(int i = 0; i<data.size(); i++){
                if (i>counter){
                    newarraylist.add(data.get(i));
                }
            }
        }

        return newarraylist;
    }

    public static String downloadFromLink(String url, String path) throws URISyntaxException {
        String filename = Paths.get(new URI(url).getPath()).getFileName().toString(); // getting the filepath name // https://stackoverflow.com/a/33871029
        String saving_to = path + filename;
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(saving_to), StandardCopyOption.REPLACE_EXISTING); // https://stackoverflow.com/a/17169576
        } catch (Exception e){
            System.out.println("Icon error : " + e);
        }

        return saving_to;
    }

    // downloads file, default to temp/downloads
    public static String downloadFromLink(String url) throws URISyntaxException {
        String filename = Paths.get(new URI(url).getPath()).getFileName().toString(); // getting the filepath name // https://stackoverflow.com/a/33871029
        String saving_to = "data/temp/downloads/" + filename; // default save location

        return downloadFromLink(url, saving_to);
    }


    // https://stackoverflow.com/a/10967469
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String paddGain(String text){
        // padding the results
        float percentage_gain = Float.parseFloat(text);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String gain;

        if (percentage_gain > 0){
            float rounded = Float.parseFloat(df.format(percentage_gain));
            gain = "&nbsp;<img src='" + new File("data/default/profit.png").toURI() + "' width='9' height='10'> " + rounded + "%";
        } else {
            float rounded = Float.parseFloat(df.format(percentage_gain));
            gain = "<img src='" + new File("data/default/loss.png").toURI() + "' width='9' height='9'>" + rounded + "%";
        }
        return gain;
    }




}
