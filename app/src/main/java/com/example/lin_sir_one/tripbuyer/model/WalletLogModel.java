package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by linSir on 16/8/11.钱包交易详情界面
 */
public class WalletLogModel {

    double balance;
    String createDate;
    String purseLogId;
    String sum;
    String symbol;
    String target;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPurseLogId() {
        return purseLogId;
    }

    public void setPurseLogId(String purseLogId) {
        this.purseLogId = purseLogId;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
