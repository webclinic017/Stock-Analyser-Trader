import pandas_datareader as web



symbol = 'AAPL'
df = web.DataReader(symbol, 'yahoo') # TODO: use the same source Java uses to get data for more accurate EMA, just parse the csv downloaded by Java



# using pandas' built in function

def ema(values, period):
    return values.ewm(span=period, adjust=False).mean()




print(ema(df['Close'], 20))
