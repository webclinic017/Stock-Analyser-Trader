# https://colab.research.google.com/drive/1MF_tbivSvSfXvLRLJifDzkQ_8FjWukYy?usp=sharing#scrollTo=fLnJLSLQnTxV

from alpaca_trade_api.stream import Stream
from alpaca_trade_api.common import URL

async def trade_callback(t):
    print('trade', t)

async def quote_callback(q):
    print('quote', q)

async def news_callback(q):
    # TODO: Add a parser that parses any html tags that it may contain as it's a stream, might not have been yet removed.
    print('news', q)

# Initiate Class Instance
stream = Stream("PKZLW20DRB2DJFPPRMZV",
                "HS655suik2zGGRFXbqe5b7Xc1GRT8jHFVxQxzD1g",
                base_url=URL('https://paper-api.alpaca.markets'),
                data_feed='iex')  # <- replace to 'sip' if you have PRO subscription

# subscribing to event
#stream.subscribe_trades(trade_callback, 'AAPL')
stream.subscribe_quotes(quote_callback, 'AAPL')
stream.subscribe_news(news_callback, '*')
stream.run()



# create a flask server with a url endpoint to execute trades
# from flask import Flask, request
# app = Flask(__name__)
#
# @app.route('/trade', methods=['POST'])
# def trade():
#     trade_type = request.form['trade_type']
#     symbol = request.form['symbol']
#     qty = request.form['qty']
#     side = request.form['side']
#     time_in_force = request.form['time_in_force']
#     order_class = request.form['order_class']
#     take_profit = request.form['take_profit']
#     stop_loss = request.form['stop_loss']
#     trailing_stop = request.form['trailing_stop']

#     stream.execute_trade(trade_type=trade_type, symbol=symbol, qty=qty, side=side, time_in_force=time_in_force, order_class=order_class, take_profit=take_profit, stop_loss=stop_loss, trailing_stop=trailing_stop)
#     return 'trade executed'
#
