from alpaca_trade_api.stream import Stream
from alpaca_trade_api.rest import REST
import requests
import pandas as pd
import sys
from datetime import datetime



api = REST(key_id='PKBRQ877H23MLZ6A5A44', secret_key='kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa', base_url="https://paper-api.alpaca.markets", api_version='v2')

data = {}


# symbols = sys.argv[1].split(",")

def getHistoricalData(symbol, period="1Min"): # PROBABLY THE PERIOD CAN ONLY BE 1min, YEAH! LET'S DO THIS FOR NOW!
    # TODO: DO WE WANT TO GET IT FROM ALPHAVANTAGE? OR FROM ALPACA? WHICH ONE GIVES UP UNTIL THE MOST RECENT?
    # TODO: ALSO: MAINLY, IF WE RUN THE SOFTWARE ONLY BEFORE THE MARKET OPEN, WE'LL GET THE MOST RECENT DATA WITHOUT ANY MISSES,
    # TODO: SO THIS IS A REQUIREMENT! TRADER CAN ONLY BE RAN BEFORE THE MARKET OPEN! SO NO DATA IS LOST! 
    # THIS WAY WE CAN HAVE THE MINUTE DATA EASILY FROM ALPACA AS 15MIN IS WAY PAST GONE!

    bars = api.get_bars(symbol, period, "2022-08-28", adjustment='all').df["close"].values.tolist()
    data["TSLA"] = bars





# With checks, we can just pass the symbols list and it will filter it out itself, so pass the symbols list to both functions

# find a way to get the latest price of the asset in the right timeframe and update the MAs accordingly and call the check signal method

# returns 0 hold, -1 for sell and 1 for buy
previousEMAHigherThanSMA = None
def checkSignal(symbol, data):

    lastEMA = data["TSLA-EMA"][-2]
    currentEMA = data["TSLA-EMA"][-1]

    lastSMA = data["TSLA-SMA"][-2]
    currentSMA = data["TSLA-SMA"][-1]


    if lastEMA > lastSMA:
        previousEMAHigherThanSMA = True
    
    if lastEMA < lastSMA:
        previousEMAHigherThanSMA = False


    if currentEMA > currentSMA:
        currentEMAHigherThanSMA = True
    
    if currentEMA < currentSMA:
        currentEMAHigherThanSMA = False


    if currentEMAHigherThanSMA != previousEMAHigherThanSMA: # crossover happened
        if currentEMAHigherThanSMA:
            # buy
            print("BUY SIGNAL")
            return 1
        
        if not currentEMAHigherThanSMA:
            # sell
            print("SELL SIGNAL")
            return -1
    
    print("HOLD SIGNAL")
    return 0


def calculateTradeSizeAllocation(symbol):
    buying_power = api.get_account().buying_power

    quote = api.get_latest_quote(symbol).bp # TODO: NOT THE EXACT PRICE, BUT CLOSE ENOUGH


    try:
        position = api.get_position(symbol)

        cost_basis = position.cost_basis
        if float(cost_basis) > float(buying_power)*0.40: # if already have more than 40% of the buying power in this asset, don't buy more
            return 0

    except: # no position
        pass


    return int((float(buying_power) * 0.20) / float(quote)) # BUY/SELL 20% OF BUYING POWER, WANT A INT SO QTY SO WE BUY 1 SHARE, NOT 0.5
    



def actOnSignal(symbol, signal):
    if signal == 1: # buy

        # TODO: CHECK IF WE HAVE A POSITION OR SOMETHING ELSE
        # close all positions first, then buy more
        closePosition(symbol)


        qty = calculateTradeSizeAllocation(symbol)
        print("BUYING " + str(qty) + " SHARES OF " + symbol)
        executeTrade(symbol, qty, "buy", "1min")
    if signal == -1: # sell

        # TODO: CHECK IF WE HAVE A POSITION OR SOMETHING ELSE
        # close all positions first, then short more
        closePosition(symbol)


        qty = calculateTradeSizeAllocation(symbol)
        print("SELLING " + str(qty) + " SHARES OF " + symbol)
        executeTrade(symbol, qty, "sell", "1min")
    if signal == 0:
        print("HOLD")



## SO I CAN JUST USE THE BELOW FOR THE MINUTE TRADE, JUST ADD THE CLOSE PRICE TO THE ARRAYLIST AND CALCULATE THE MAs, THEN CALL
## THE checkSignal()

## FOR OTHER TIMEFRAMES, KNOW WHEN THE NEW CANDLE IS, LIKE IT'S AT 30 MINUTES PAST FOR THE STOCKS LIKE THAT, 
## THEN QUERY THE PRICE AT THAT POINT, THEN DO THE SAME AS DESCRIBED ABOVE.

ema = 10
sma = 12

def calculateEMA(symbol, period=ema):
    global data

    priceList = data["TSLA"]
    priceList = pd.DataFrame(priceList)
    ema = pd.DataFrame()

    ema["ema"] = priceList.ewm(span=period, adjust=False).mean()

    ema = ema["ema"].tolist()

    data[str("TSLA") + "-EMA"] = ema


def calculateSMA(symbol, period=sma):
    global data

    priceList = data["TSLA"]
    # turn into pandas dataframe
    priceList = pd.DataFrame(priceList)
    sma = pd.DataFrame()
    sma["sma"] = priceList.rolling(window=period).mean()

    # convert only the sma values to list
    sma = sma["sma"].tolist()

    data[str("TSLA") + "-SMA"] = sma




# TODO: ADD THE NEW PRICE TO THE PRICE LIST AND THEN RECALCULATE THE MAs THEN CALL THE checkSignal()
def newPriceData(symbol, price):

    print(symbol, "- Close:", price)
    # add the new values

    # now = datetime.now()
    # current_time = now.strftime("%Y-%m-%d %H:%M:%S")
    # # TODO: THE TIMESTAMP IS NOT IN THE CORRECT FORMAT, BUT THE CODE WORKS
    # newData = {symbol: [price]}
    # newDataFrame = pd.DataFrame(newData)
    # print(newDataFrame)

    # data = data.append(newDataFrame, ignore_index = True)
    # print(data)

    data["TSLA"].append(price)

    calculateSMA(data)
    calculateEMA(data)
    
    
    # print(data)
    print("EMA: " + str(data[str("TSLA") + "-EMA"][-1]))
    print("SMA: " + str(data[str("TSLA") + "-SMA"][-1]))

    actOnSignal(symbol, checkSignal(symbol, data))


# TODO: HAVE A REASONING ON WHY IT'S NOT USING THE LOCAL API TO EXECUTE TRADES, LIKE STANDALONE SOFTWARE, OR SOMETHING LIKE THAT
def executeTrade(symbol, qty, side, onTraderCandleType):
    data = requests.get("http://localhost:5000/trade?symbol=" + symbol + "&qty=" + str(qty) + "&side=" + side + "&onTraderCandleType=" + onTraderCandleType)
    return data.text # maybe return True/False


def closePosition(symbol):
    data = requests.get("http://localhost:5000/close-position?symbol=" + symbol)
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
    
    # stream.subscribe_quotes(print_quote, 'AAPL')
    # stream.subscribe_trades(print_trade, 'AAPL')
    # stream.subscribe_crypto_trades(print_trade, 'BTCUSD')
    

    ## UPON TESTING, THIS RETURNS 3 PRICES FROM THREE DIFFERENT EXCHANGES: ERSX, CBSE, FTXU
    # i'VE NOW GOT TO CHOOSE ONE OF THEM, PROBABLY JUST WHICH HISTORICAL DATA I HAVE THE MAs BASED ON
    # @stream.on_crypto_bar('BTCUSD')  ###### THIS WORKS ##### prints every 1 minute at 30 seconds
    # async def _(bar):
    #     print('BAR CRYPTO\n', bar)
    #     newPriceData(bar.symbol, bar.close)

    # retruns price every minute at 00 seconds
    @stream.on_bar(symbol)
    async def _(bar):
        print('BAR STOCK\n', bar)  
        newPriceData(bar.symbol, bar.close)
        
    stream.run()


if __name__ == "__main__":
    # GETTING DATA
    symbol = "TSLA"
    getHistoricalData(symbol)
    
    main()
