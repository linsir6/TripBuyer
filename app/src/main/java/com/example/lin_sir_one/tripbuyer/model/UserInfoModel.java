package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 2016/7/7.专门用来做用户缓存的类
 */
public class UserInfoModel {

    private String uid;         //用户的唯一id
    private String name;        //用户的姓名
    private String head;        //用户的头像
    private String isReal;      //是否身份信息验证

    public UserInfoModel(String uid, String name, String head, String isReal) {
        this.uid = uid;
        this.name = name;
        this.head = head;
        this.isReal = isReal;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getIsReal() {
        return isReal;
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }
}
