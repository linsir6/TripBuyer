package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 2016/7/9.登陆成功返回的obj
 */
public class Obj {

    String head;                    //用户的头像
    String name;                    //用户姓名
    String tel;                     //用户手机号
    String uid;                     //用户的uid
    String wid;                     //订单的id号
    String deliveryArea;            //发货地区
    int deliveryTime;               //发货时间
    String num;                     //总数量
    int pack;                       //有无包装
    String picture;                 //商品图片
    String price;                   //商品价格
    String productName;             //商品名称
    String requirements;            //要求
    String status;
    Double total;
    String isReal;
    String instruction;
    String mainCity;
    String perference;
    String returnDate;
    String rid;
    String startPlace;
    String travelDate;
    String travelPlace;
    String orderId;

    double balance;                    //余额
    int isHaveAli;                  //是否绑定了支付宝
    int isHavePassword;             //是否具有支付密码
    int purseId;                    //钱包id

    DepositPayModel depositPay;
    UserModel user;
    UserModel buyUser;
    UserModel sellerUser;

    String appid;
    String nonce_str;

    String mch_id;
    String prepay_id;
    int retcode;
    String retmsg;
    String sign;
    String timestamp;
    String bindingAlipay;

    OrderOverModel orderOver;


    public OrderOverModel getOrderOver() {
        return orderOver;
    }

    public void setOrderOver(OrderOverModel orderOver) {
        this.orderOver = orderOver;
    }

    public UserModel getBuyUser() {
        return buyUser;
    }

    public void setBuyUser(UserModel buyUser) {
        this.buyUser = buyUser;
    }

    public UserModel getSellerUser() {
        return sellerUser;
    }

    public void setSellerUser(UserModel sellerUser) {
        this.sellerUser = sellerUser;
    }

    public String getBindingAlipay() {
        return bindingAlipay;
    }

    public void setBindingAlipay(String bindingAlipay) {
        this.bindingAlipay = bindingAlipay;
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

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getIsReal() {
        return isReal;
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getMainCity() {
        return mainCity;
    }

    public void setMainCity(String mainCity) {
        this.mainCity = mainCity;
    }

    public String getPerference() {
        return perference;
    }

    public void setPerference(String perference) {
        this.perference = perference;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getTravelPlace() {
        return travelPlace;
    }

    public void setTravelPlace(String travelPlace) {
        this.travelPlace = travelPlace;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public DepositPayModel getDepositPay() {
        return depositPay;
    }

    public void setDepositPay(DepositPayModel depositPay) {
        this.depositPay = depositPay;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}