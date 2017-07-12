package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 2016/7/7.结果中不带有list的网络返回结果
 */
public class PayModel {

    private int code;
    private String msg;
    private String obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}
