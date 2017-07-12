package com.example.lin_sir_one.tripbuyer.api;

/**
 * Created by linSir on 2016/6/29.网络协议
 */

import com.example.lin_sir_one.tripbuyer.model.AllAddress;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.model.PayModel;
import com.example.lin_sir_one.tripbuyer.model.ResponseModel;
import com.example.lin_sir_one.tripbuyer.model.ResponseModel_no_list;
import com.example.lin_sir_one.tripbuyer.model.WalletLogModel;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface Api {

    @FormUrlEncoded
    @POST("getCode") Observable<ResponseModel_no_list> getCode(@Field("tel") String tel);

    @FormUrlEncoded
    @POST("register") Observable<ResponseModel_no_list> register(@Field("tel") String tel, @Field("password") String password
            , @Field("code") String code);

    @FormUrlEncoded
    @POST("validate") Observable<ResponseModel_no_list> validate(@Field("tel") String tel, @Field("code") String code);

    @FormUrlEncoded
    @POST("login") Observable<ResponseModel_no_list> login_usepwd(@Field("tel") String tel, @Field("password") String password);

    @FormUrlEncoded
    @POST("login") Observable<ResponseModel_no_list> login_usecode(@Field("tel") String tel, @Field("code") String password);

    @FormUrlEncoded
    @POST("famous") Observable<ResponseModel<List<FamousPageModel>>> famous22(@Field("page") int page);

    @FormUrlEncoded
    @POST("allWanted") Observable<ResponseModel<List<FamousPageModel>>> allWanted(@Field("page") int page, @Field("field") String field, @Field("order") String order);

    @FormUrlEncoded
    @POST("wanted") Observable<ResponseModel_no_list> wanted(@Field("productName") String productName
            , @Field("price") String price
            , @Field("num") String num
            , @Field("userAddress.addressId") String addressId
            , @Field("deliveryTime") int deliveryTime
            , @Field("pack") int pack
            , @Field("deliveryArea") String deliveryArea
            , @Field("requirements") String requirements
            , @Field("picture") String picture
            , @Field("total") Double total
            , @Field("createType") String createType
    );


    @FormUrlEncoded
    @POST("weiPay/wantedPay") Observable<ResponseModel_no_list> weiXinquankuan(@Field("id") String id);


    @FormUrlEncoded
    @POST("allAddress") Observable<ResponseModel<List<AllAddress>>> allAddress(@Field("wid") int noMain);


    @FormUrlEncoded
    @POST("address") Observable<ResponseModel_no_list> address(@Field("shipName") String shipName, @Field("province") String province
            , @Field("city") String city, @Field("area") String area, @Field("detail") String detail
            , @Field("phone") String phone, @Field("zip") String zip, @Field("defaultAddress") int defaultAddress);


    @FormUrlEncoded
    @POST("route") Observable<ResponseModel_no_list> route(@Field("travelDate") String travelDate, @Field("startPlace") String startPlace
            , @Field("travelPlace") String travelPlace, @Field("mainCity") String mainCity, @Field("returnDate") String returnDate
            , @Field("perference") String perference, @Field("instruction") String instruction);


    @FormUrlEncoded
    @POST("famous") Observable<ResponseModel<List<JourneyModel>>> famous2(@Field("page") int page);


    @GET("famous") Observable<ResponseModel<List<JourneyModel>>> route2(@Query("id") String id);

    @GET("wanted/{id}") Observable<ResponseModel_no_list> wanted2(@Path("id") String id);

    @GET("order/{orderId}") Observable<ResponseModel_no_list> meOrderDetails(@Path("orderId") String orderId);

    @FormUrlEncoded
    @POST("alipay/wantedPay") Observable<PayModel> wantedPay(@Field("wid") String wid);

    @FormUrlEncoded
    @POST("purse") Observable<ResponseModel_no_list> wallet(@Field("a") String wid);

    @FormUrlEncoded
    @POST("purse/log") Observable<ResponseModel<List<WalletLogModel>>> walletLog(@Field("page") String page);

    @FormUrlEncoded
    @POST("q") Observable<ResponseModel<List<FamousPageModel>>> q1(@Field("page") String page, @Field("query") String querypage, @Field("field") String field, @Field("order") String order);

    @FormUrlEncoded
    @POST("my") Observable<ResponseModel<List<JourneyModel>>> meRoute(@Field("page") String page, @Field("rows") String rows);

    @FormUrlEncoded
    @POST("my") Observable<ResponseModel<List<FamousPageModel>>> meReward(@Field("page") String page, @Field("rows") String rows);

    @FormUrlEncoded
    @PUT("update") Observable<ResponseModel_no_list> updateLoginPwd(@Field("oldPassword") String oldPwd, @Field("password") String newPwd);

    @FormUrlEncoded
    @PUT("update") Observable<ResponseModel_no_list> updateName(@Field("name") String oldPwd);

    @FormUrlEncoded
    @PUT("update") Observable<ResponseModel_no_list> updateHead(@Field("head") String head);

    @FormUrlEncoded
    @PUT("purse") Observable<ResponseModel_no_list> purse(@Field("name") String oldPwd);

    @FormUrlEncoded
    @PUT("order/place") Observable<ResponseModel_no_list> place(@Field("wid") String wid);


    @FormUrlEncoded
    @POST("certification") Observable<ResponseModel_no_list> certification(@Field("realName") String realName, @Field("carded") String carded, @Field("cardedPicture") String cardedPicture, @Field("passportPicture") String passportPicture);

    @FormUrlEncoded
    @POST("password") Observable<ResponseModel_no_list> password(@Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("order/my") Observable<ResponseModel<List<FamousPageModel>>> meOrder(@Field("page") String page, @Field("rows") String rows);

    @GET("route/{id}") Observable<ResponseModel_no_list> sellerDetails(@Path("id") String id);

    @FormUrlEncoded
    @POST("alipay/depositPay") Observable<PayModel> depositPay(@Field("orderId") String orderId);

//    @FormUrlEncoded
//    @POST("weiPay/wantedPay") Observable<ResponseModel_no_list> weiXin_xuanShang(@Field("id") String orderId);

    @FormUrlEncoded
    @PUT("purse/bindingAli") Observable<ResponseModel_no_list> bindingAli(@Field("newAliNumber") String newAliNumber);

    @FormUrlEncoded
    @POST("purse/bindingAli") Observable<ResponseModel_no_list> bindingAliNew(@Field("newAliNumber") String newAliNumber);

    @FormUrlEncoded
    @POST("weiPay/wantedPay") Observable<ResponseModel_no_list> weiXin_xuanShang(@Field("id") String orderId);

    @FormUrlEncoded
    @POST("purse/withdraw") Observable<PayModel> withdraw(@Field("sum") String sum, @Field("password") String password);

    @FormUrlEncoded
    @POST("purse/newWithdraw") Observable<PayModel> newWithdraw(@Field("realName") String surelName,@Field("password") String password,@Field("sum") String sum, @Field("alipay") String alipay);

    @FormUrlEncoded
    @POST("purse/password") Observable<ResponseModel_no_list> setPayPwd(@Field("newPassword") String newPassword);

    @FormUrlEncoded
    @PUT("purse/password") Observable<ResponseModel_no_list> modifyPwd(@Field("newPassword") String newPassword);

    @DELETE("buyer/api/wanted/{id}") Observable<ResponseModel_no_list> deleteReward(@Path("id") String id);

    @DELETE("seller/api/route/{id}") Observable<ResponseModel_no_list> deleteRoute(@Path("id") String id);

    @FormUrlEncoded
    @POST("order/confirm") Observable<ResponseModel_no_list> sureShouHuo(@Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("order/upload") Observable<ResponseModel_no_list> sureFaHuo(@Field("orderOverId") String orderOverId,
                                                                      @Field("airTicket") String airTicket,
                                                                      @Field("expressNumber") String expressNumber,
                                                                      @Field("orderId") String orderId);


}


















