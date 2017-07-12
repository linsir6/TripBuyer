package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by linSir on 16/8/27.paymodel
 */
public class DepositPayModel {

    String depositId;
    String depositPayId;
    String payamount;
    String paystatus;


    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getDepositPayId() {
        return depositPayId;
    }

    public void setDepositPayId(String depositPayId) {
        this.depositPayId = depositPayId;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }
}
