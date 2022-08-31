from alpaca_trade_api.stream import Stream
import requests
import sys

# symbols = sys.argv[1].split(",")
# With checks, we can just pass the symbols list and it will filter it out itself, so pass the symbols list to both functions

# find a way to get the latest price of the asset in the right timeframe and update the MAs accordingly and call the check signal method

# returns 0 hold, -1 for sell and 1 for buy
def checkSignal(data):
    pass

## SO I CAN JUST USE THE BELOW FOR THE MINUTE TRADE, JUST ADD THE CLOSE PRICE TO THE ARRAYLIST AND CALCULATE THE MAs, THEN CALL
## THE checkSignal()

## FOR OTHER TIMEFRAMES, KNOW WHEN THE NEW CANDLE IS, LIKE IT'S AT 30 MINUTES PAST FOR THE STOCKS LIKE THAT, 
## THEN QUERY THE PRICE AT THAT POINT, THEN DO THE SAME AS DESCRIBED ABOVE.


# TODO: ADD THE NEW PRICE TO THE PRICE LIST AND THEN RECALCULATE THE MAs THEN CALL THE checkSignal()
def newPriceData(symbol, price):
    print(symbol, "- Close:", price)
    # add the new values
    # calculateMA(data)
    # checkSignal(data)


def executeTrade(symbol, qty, side, onTraderCandleType):
    data = requests.get("http://localhost:5000/trade?symbol=" + symbol + "&qty=" + qty + "&side=" + side + "&onTraderCandleType=" + onTraderCandleType)
    return data.text # maybe return True/False

# TODO: GET THE HISTORICAL DATA AND HAVE THEM ALL STORED IN A SINGLE DATA FRAME
# TODO: ADD LOGIC PERHAPS THAT COUNTS THE NUMBER OF 1 MIN CANDLES, WHEN REACHES 1H, CALLS THE FUNCTION TO TRADE 1H TIMEFRAME


api_key = "PKBRQ877H23MLZ6A5A44"
api_secret = "kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa"
base_url = 'https://paper-api.alpaca.markets'


# maybe just have these going on, looks sick on the terminal
async def print_trade(t):
    print('Trade ', t)

async def print_quote(q):
    print('Quote ', q)



def main():
    feed = 'iex' 

    stream = Stream(api_key,
                api_secret,
                base_url=base_url,
                data_feed='iex') 
    
    stream.subscribe_quotes(print_quote, 'AAPL')
    # stream.subscribe_trades(print_trade, 'AAPL')
    # stream.subscribe_crypto_trades(print_trade, 'BTCUSD')
    

    ## UPON TESTING, THIS RETURNS 3 PRICES FROM THREE DIFFERENT EXCHANGES: ERSX, CBSE, FTXU
    # i'VE NOW GOT TO CHOOSE ONE OF THEM, PROBABLY JUST WHICH HISTORICAL DATA I HAVE THE MAs BASED ON
    @stream.on_crypto_bar('BTCUSD')  ###### THIS WORKS ##### prints every 1 minute at 30 seconds
    async def _(bar):
        print('BAR CRYPTO\n', bar)
        newPriceData(bar.symbol, bar.close)

    # retruns price every minute at 00 seconds
    @stream.on_bar('AAPL')
    async def _(bar):
        print('BAR STOCK\n', bar)  
        newPriceData(bar.symbol, bar.close)
        
        
    stream.run()


if __name__ == "__main__":
    main()
