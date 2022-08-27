# find a way to get the latest price of the asset in the right timeframe and update the MAs accordingly and call the check signal method

# returns 0 hold, -1 for sell and 1 for buy
def checkSignal(data):
    pass


from alpaca_trade_api.stream import Stream

api_key = "PKBRQ877H23MLZ6A5A44"
api_secret = "kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa"
base_url = 'https://paper-api.alpaca.markets'


async def print_trade(t):
    print('trade', t)


async def print_quote(q):
    print('quote', q)


async def print_trade_update(tu):
    print('trade update', tu)


async def print_crypto_trade(t):
    # print('crypto trade', t)
    pass


    


def main():
    feed = 'iex'  #  < - replace to SIP if you have PRO subscription
    

    stream = Stream(api_key,
                api_secret,
                base_url=base_url,
                data_feed='iex') 
    
    stream.subscribe_trade_updates(print_trade_update)
    stream.subscribe_trades(print_trade, 'AAPL')
    stream.subscribe_quotes(print_quote, 'IBM')
    stream.subscribe_crypto_trades(print_crypto_trade, 'BTCUSD')

    @stream.on_bar('MSFT')
    async def _(bar):
        print('bar', bar)  
        
        
    ##### test this when market open #####
    @stream.on_updated_bar('MSFT')
    async def _(bar):
        print('updated bar', bar)
        
    @stream.on_crypto_updated_bar('BTCUSD')  ###### THIS WORKS ##### prints every 1 minute at 30 seconds
    async def _(bar):
        print('updated bar crypto', bar)

    @stream.on_status("*")
    async def _(status):
        print('status', status)

    @stream.on_luld('AAPL', 'MSFT')
    async def _(luld):
        print('LULD', luld)
        
    @stream.subscribe_crypto_daily_bars('BTCUSD')
    async def _(bar):
        print('daily bar crypto', bar)
        
    @stream.on_bar('AAPL') ### TRY THIS ### WHEN MARKET OPEN
    async def _(bar):
        print('bar STOCK', bar)
        
    @stream.on_daily_bar('AAPL') ### TRY THIS ### WHEN MARKET OPEN
    async def _(bar):
        print('daily bar STOCK', bar)
        
        
    stream.run()


if __name__ == "__main__":
    main()
