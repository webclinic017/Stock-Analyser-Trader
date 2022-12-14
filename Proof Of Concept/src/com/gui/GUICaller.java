package com.gui;

import com.asset.Asset;
import com.utils.Cache;

import javax.swing.*;
import java.lang.Thread;


public class GUICaller {
    JFrame homescreen_frame;

    public GUICaller(){
    }

    public void startup(){
        // TODO: Start everything off here, manage caching etc... Start instantiating assets from watchlist etc... and save it a arraylist

        new Thread(Cache::resetExpiredURLs).start();

        // starting a thread to load the Homescreen while the user logs in.
        new Thread(() -> {
            try {
                homescreen_frame = GUICaller.HomeScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Login();
    }

    public static JFrame HomeScreen() throws Exception {
        System.out.println("SEQUENCE: Home Screen");
        JFrame frame = new JFrame("Home Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HomeScreen myGUI = new HomeScreen(850, 450);
        frame.add(myGUI);
        frame.pack();
        //frame.setVisible(true); // setVisible is set to false so that this function can be called from login so it loads the GUI but not display it until the login is complete
        return frame; // returning the frame so that it can be set to visible later
    }

    public static void AssetInfo(Asset asset) throws Exception {
        System.out.println("SEQUENCE: Stock Info " + asset.ticker);
        JFrame frame = new JFrame("Stock");
        AssetInfo myGUI = new AssetInfo(600, 630, asset);
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }

    public void setHomeScreenVisible(boolean option) throws InterruptedException {
        while(true) {
            try {
                homescreen_frame.setVisible(option);
                break;
            } catch (Exception ignored) {
            }

            Thread.sleep(250);
        }

    }

    public static void SimulationResults(Asset asset) throws Exception {
        System.out.println("SEQUENCE: SimulationResults");
        JFrame frame = new JFrame("Simulation Results");
        SimulationResults myGUI = new SimulationResults(440, 540, asset);
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }

    public static void Simulate(Asset asset, int sma1, int sma2, String maType1, String maType2) throws Exception {
        System.out.println("SEQUENCE: Simulate");
        JFrame frame = new JFrame("Backdating");
        SimulateGraphically myGUI = new SimulateGraphically(930, 630, asset, sma1, sma2, maType1, maType2);
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }

    public static void CustomizeSimluation(Asset asset) throws Exception {
        System.out.println("SEQUENCE: Custom Simulation");
        JFrame frame = new JFrame("Custom Simulation");
        CustomSimulation myGUI = new CustomSimulation(280, 150, asset);
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }

    public static void Trader(Asset asset) throws Exception {
        System.out.println("SEQUENCE: Trader");
        JFrame frame = new JFrame("Trader");
        MassSimulate myGUI = new MassSimulate(280, 150, asset);
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }


    public void Login(){
        System.out.println("SEQUENCE: Login");
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Login myGUI = new Login(350, 140, frame, this); // passing the frame so that it can close itself...
        frame.add(myGUI);
        frame.pack();
        frame.setVisible(true);
    }
}
