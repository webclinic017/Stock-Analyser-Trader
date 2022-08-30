import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import pandas_datareader as web

# https://plainenglish.io/blog/how-to-calculate-the-ema-of-a-stock-with-python
def calculate_ema(prices, days, smoothing=2):
    ema = [sum(prices[:days]) / days]
    for price in prices[days:]:
        ema.append((price * (smoothing / (1 + days))) + ema[-1] * (1 - (smoothing / (1 + days))))
    return ema

symbol = 'AAPL'
df = web.DataReader(symbol, 'yahoo') # TODO: use the same source Java uses to get data for more accurate EMA, just parse the csv downloaded by Java

ema = calculate_ema(df['Close'], 20)
print(ema)
