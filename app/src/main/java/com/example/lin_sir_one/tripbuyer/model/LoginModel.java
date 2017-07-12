package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 2016/7/7.登录成功返回的模版
 */
public class LoginModel {

    private String head;
    private String name;
    private String tel;
    private String uid;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginModel(String head, String name, String tel, String uid) {
        this.head = head;
        this.name = name;
        this.tel = tel;
        this.uid = uid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}