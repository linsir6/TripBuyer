package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by linSir on 16/8/10.我的钱包
 */
public class WalletModel {

    private double balance;
    private int isHaveAli;
    private int isHavePassword;
    private int purseId;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIsHaveAli() {
        return isHaveAli;
    }

    public void setIsHaveAli(int isHaveAli) {
        this.isHaveAli = isHaveAli;
    }

    public int getIsHavePassword() {
        return isHavePassword;
    }

    public void setIsHavePassword(int isHavePassword) {
        this.isHavePassword = isHavePassword;
    }

    public int getPurseId() {
        return purseId;
    }

    public void setPurseId(int purseId) {
        this.purseId = purseId;
    }
}
