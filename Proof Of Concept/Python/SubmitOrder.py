from alpaca.trading.client import TradingClient
from alpaca.trading.requests import MarketOrderRequest
from alpaca.trading.enums import OrderSide, TimeInForce

trading_client = TradingClient('PKBRQ877H23MLZ6A5A44', 'kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa')


# preparing order data
market_order_data = MarketOrderRequest(
                      symbol="META",
                      qty=1,
                      side=OrderSide.BUY,
                      time_in_force=TimeInForce.DAY
                  )

# Market order
market_order = trading_client.submit_order(
                order_data=market_order_data
                )

# way to send a market order with money value instead of quantity
# api.submit_order(
#     symbol='SPY',
#     notional=450,  # notional value of 1.5 shares of SPY at $300
#     side='buy',
#     type='market',
#     time_in_force='day',
# )