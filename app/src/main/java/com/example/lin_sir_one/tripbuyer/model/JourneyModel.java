package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by linSir on 16/7/25.获取行程界面的列表
 */
public class JourneyModel {

    private String mainCity;
    private String returnDate;
    private String rid;
    private String startPlace;
    private String travelDate;
    private String travelPlace;
    private UserInfoModel user;
    private String createDate;
    private String instruction;
    private String perference;

    public String getMainCity() {
        return mainCity;
    }

    public void setMainCity(String mainCity) {
        this.mainCity = mainCity;
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

    public UserInfoModel getUser() {
        return user;
    }

    public void setUser(UserInfoModel user) {
        this.user = user;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPerference() {
        return perference;
    }

    public void setPerference(String perference) {
        this.perference = perference;
    }
}
