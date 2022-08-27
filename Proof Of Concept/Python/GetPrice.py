from alpaca_trade_api.rest import REST
import pandas as pd

api = REST('PKBRQ877H23MLZ6A5A44', 'kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa')

data = api.get_bars("AAPL", "1Day", "2021-01-01", "2022-08-26", adjustment='raw').df

SMA1 = pd.DataFrame()
SMA1["close"] = data["close"].rolling(window=50).mean()


SMA2 = pd.DataFrame()
SMA2["close"] = data["close"].rolling(window=180).mean()


combined = pd.DataFrame()
data["AAPL"] = data["close"]
data["SMA1"] = SMA1["close"]
data["SMA2"] = SMA2["close"]


print(data)
