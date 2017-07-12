package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 2016/7/8.明星买家界面
 */
public class FamousPageModel {


    private String num;             //代购产品数量
    private String productName;     //商品名称
    private String total;           //总价
    private String picture;          //头像
    private String name;            //昵称
    private String wid;
    private String status;          //物品状态
    private UserInfoModel buyUser;  //买家的信息
    private String orderId;
    private int deliveryTime;               //发货时间

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public UserInfoModel getBuyUser() {
        return buyUser;
    }

    public void setBuyUser(UserInfoModel buyUser) {
        this.buyUser = buyUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String uid;             //uid
    private UserModel user;


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHead() {
        return picture;
    }

    public void setHead(String head) {
        this.picture = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
