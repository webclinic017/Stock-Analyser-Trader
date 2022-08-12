package com.trader;

import com.asset.Asset;

// TODO: Say in critetion made this outside of all of the things as the broker might change in the future the main asset class doesn't need to change

public class Execute {

    public Execute(){
    }

    public boolean order(Asset asset, int amount, String side){ // side, either buy or sell
        // TODO: Generate client_order_id and save it in the logs, so can it associated later on why the trade was executed?
        String data = "symbol="+asset+"&qty="+amount+"&side="+side+"&type=market"; // TODO: work in progress

        try {
            // TODO: Execute the trade...
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
