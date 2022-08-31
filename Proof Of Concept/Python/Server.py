from flask import Flask, request
import alpaca_trade_api as tradeapi
import random


api_key = "PKBRQ877H23MLZ6A5A44"
api_secret = "kYASo3caUfQ6yRdgMLC72aFkaXo7T7K9mCIK9pRa"
base_url = 'https://paper-api.alpaca.markets'


app = Flask(__name__)


api = tradeapi.REST(api_key, api_secret, base_url, api_version='v2')

@app.route('/trade', methods=['GET'])
def trade():
    symbol = request.args.get('symbol')
    qty = request.args.get('qty')
    side = request.args.get('side')
    onTraderCandleType = request.args.get('onTraderCandleType') # record of what Trader bought the asset
    # generate random 38 characters
    randomchars = onTraderCandleType + "-" + ''.join(random.choice('0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ') for i in range(40))
    print(randomchars)

    try:
        answer = api.submit_order(symbol, qty=qty, side=side, time_in_force='gtc', client_order_id=randomchars)
        return "Success"
    except Exception as e:
        return str(e)    



if __name__ == '__main__':
    app.run(debug=True)
    