package com.trader;

import com.asset.Asset;

// TODO: Say in critetion made this outside of all of the things as the broker might change in the future the main asset class doesn't need to change

public class Execute {

    public Execute(){
    }

    public boolean buy(Asset asset, int amount){
        try {
            // TODO: Execute the trade...
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean sell(Asset asset, int amount){
        try {
            // TODO: Execute the trade...
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
