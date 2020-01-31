package com.bitstamp;

public class BidAskModel {
    float bidAmount;
    float bidPrice;
    float askAmount;
    float askPrice;


    public BidAskModel(float bidAmount, float bidPrice, float askAmount, float askPrice) {
        this.bidAmount = bidAmount;
        this.bidPrice = bidPrice;
        this.askAmount = askAmount;
        this.askPrice = askPrice;
    }

    public float getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(float bidAmount) {
        this.bidAmount = bidAmount;
    }

    public float getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public float getAskAmount() {
        return askAmount;
    }

    public void setAskAmount(float askAmount) {
        this.askAmount = askAmount;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }
}
