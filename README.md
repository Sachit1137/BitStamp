# ILoveZappos
Cryptocurrency Android application

Requirements:

1. Transaction History

a. Display a line or bar graph showing the price history over time

b. API to use: https://www.bitstamp.net/api/v2/transactions/btcusd/

c. See the Currency Value graph at https://www.bitstamp.net/ for an example

d. We’d like you to use mpAndroidChart to create your graph


2. Order Book

a. Display 2 tables using RecyclerViews; Bids and Asks

b. API to use: https://www.bitstamp.net/api/v2/order_book/btcusd/

c. See the Live Order Book tables at https://www.bitstamp.net/ for an example


3. Price Alert

a. Create a way to take price input from the user and store it using some sort of

storing mechanism. Hit the api mentioned below every hour (in the background

using a Service) and if the current bitcoin price has fallen below the specified

price entered by the user, the app should send a notification which would open

the app when clicked.

b. API to use: https://www.bitstamp.net/api/v2/ticker_hour/btcusd/

c. Hint: You could use FirebaseJobDispatcher to implement this
