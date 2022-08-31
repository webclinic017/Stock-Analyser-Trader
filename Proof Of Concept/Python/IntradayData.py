from alpaca.data.historical import StockHistoricalDataClient
from alpaca.data.requests import StockBarsRequest
from alpaca.data.timeframe import TimeFrame

client = StockHistoricalDataClient('PKBRQ877H23MLZ6A5A44', 'kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa')

request_params = StockBarsRequest(
                        symbol_or_symbols=["AAPL"],
                        timeframe=TimeFrame.Minute,
                        start="2022-08-01"
                        )

bars = client.get_stock_bars(request_params)

# convert to dataframe
data = bars.df

data.to_csv("AAPL.csv")

