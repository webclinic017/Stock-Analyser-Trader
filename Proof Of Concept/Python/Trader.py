import sys


# symbols = sys.argv[1].split(",")
# TODO: find out whether the symbol is a stock or crypto and pass it to the respective function
# OR JUST MAKE JAVA SEND THEM SEPERATELY... SO LIKE SEPERATE THREADS FOR SEPERATE ASSETS
# TODO: YEAH DEFINATELY DO THAT: SO LIKE THE ARGS WILL BE SOMETHING LIKE: stocks: AAPL,GOOG,MSFT,FB,AMZN,TSLA,NFLX crypto: BTCUSD, ETHUSD
# AND IT'S FINE IF THERE AREN'T EVERY ASSET CLASS, IT WILL JUST THROW A ERROR SILENTLY

# find a way to get the latest price of the asset in the right timeframe and update the MAs accordingly and call the check signal method

# returns 0 hold, -1 for sell and 1 for buy
def checkSignal(data):
    pass

## SO I CAN JUST USE THE BELOW FOR THE MINUTE TRADE, JUST ADD THE CLOSE PRICE TO THE ARRAYLIST AND CALCULATE THE MAs, THEN CALL
## THE checkSignal()

## FOR OTHER TIMEFRAMES, KNOW WHEN THE NEW CANDLE IS, LIKE IT'S AT 30 MINUTES PAST FOR THE STOCKS LIKE THAT, 
## THEN QUERY THE PRICE AT THAT POINT, THEN DO THE SAME AS DESCRIBED ABOVE.

# request the existing flask server... WE WANT THIS SEPERATE AND STAND ALONE AS IT CAN BE RAN ON THE SERVER 24/7
def order():
    pass


# TODO: ADD THE NEW PRICE TO THE PRICE LIST AND THEN RECALCULATE THE MAs THEN CALL THE checkSignal()
def newPriceData(symbol, price):
    print(symbol, "- Close:", price)
    # add the new values
    # calculateMA(data)
    # checkSignal(data)


# TODO: GET THE HISTORICAL DATA AND HAVE THEM ALL STORED IN A SINGLE DATA FRAME


from alpaca_trade_api.stream import Stream

api_key = "PKBRQ877H23MLZ6A5A44"
api_secret = "kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa"
base_url = 'https://paper-api.alpaca.markets'


# maybe just have these going on, looks sick on the terminal
async def print_trade(t):
    print('trade', t)

async def print_quote(q):
    print('quote', q)

async def print_trade_update(tu):
    print('trade update', tu)

async def print_crypto_trade(t):
    print('crypto trade', t)


    


def main():
    feed = 'iex'  #  < - replace to SIP if you have PRO subscription
    

    stream = Stream(api_key,
                api_secret,
                base_url=base_url,
                data_feed='iex') 
    
    # stream.subscribe_trade_updates(print_trade_update)
    # stream.subscribe_trades(print_trade, 'AAPL')
    # stream.subscribe_quotes(print_quote, 'IBM')
    # stream.subscribe_crypto_trades(print_crypto_trade, 'BTCUSD')


    ## UPON TESTING, THIS RETURNS 3 PRICES FROM THREE DIFFERENT EXCHANGES: ERSX, CBSE, FTXU
    # i'VE NOW GOT TO CHOOSE ONE OF THEM, PROBABLY JUST WHICH HISTORICAL DATA I HAVE THE MAs BASED ON
    @stream.on_crypto_bar('')  ###### THIS WORKS ##### prints every 1 minute at 30 seconds
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
