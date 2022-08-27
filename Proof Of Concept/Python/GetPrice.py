from alpaca_trade_api.rest import REST
api = REST('PKBRQ877H23MLZ6A5A44', 'kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa')

data = api.get_bars("AAPL", "1Min", "2021-01-01", "2022-08-26", adjustment='raw').df


print(data)
