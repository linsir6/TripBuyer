package com.example.lin_sir_one.tripbuyer.network;

import android.content.Context;
import android.util.Log;

import com.example.lin_sir_one.tripbuyer.api.Api;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.model.PayModel;
import com.example.lin_sir_one.tripbuyer.model.ResponseModel;
import com.example.lin_sir_one.tripbuyer.model.ResponseModel_no_list;
import com.example.lin_sir_one.tripbuyer.model.WalletLogModel;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lin_sir on 2016/8/3.所有的网络操作接口
 */
public class A {

    private Api mApi;
    private Context mContext;
    private static A mInstance;
    private int baseUrl;            //主url是哪个     1.base    2.seller    3.buyer
    private int intercept;          //是否拦截响应头   1.拦截    0.不拦截
    private int add;                //是否添加请求头   1.添加    0.不添加


    private A(int baseUrl, int intercept, int add) {
        this.mContext = BaseApplication.get().getAppContext();
        this.baseUrl = baseUrl;
        this.intercept = intercept;
        this.add = add;
        mApi = RetrofitClient.getClient(mContext, baseUrl, intercept, add).create(Api.class);
    }


    public static A getA(int baseUrl, int intercept, int add) {
        mInstance = new A(baseUrl, intercept, add);
        return mInstance;
    }

    /**
     * 获取验证码
     */
    public void getCode(HttpResultListener<Boolean> listener, final String tel) {
        mApi.getCode(tel)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 获取明星界面
     */
    public void famous22(HttpResultListener<List<FamousPageModel>> listener, int page) {
        mApi.famous22(page)
                .map(new HttpResultFunc<List<FamousPageModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取明星界面
     */
    public void allWanted(HttpResultListener<List<FamousPageModel>> listener, int page, String field, String order) {
        mApi.allWanted(page, field, order)
                .map(new HttpResultFunc<List<FamousPageModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 注册的接口
     */
    public void register(HttpResultListener<Boolean> listener, final String tel, final String password, final String code) {
        mApi.register(tel, password, code)
                .map(new HttpResultFuncNoList_has_obj())
                .map(new Func1<Obj, Boolean>() {
                    @Override
                    public Boolean call(Obj obj) {
                        if (obj != null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 用密码登录的接口
     */
    public void login_usepwd(HttpResultListener<Boolean> subscriber, final String tel, final String code) {
        mApi.login_usepwd(tel, code)
                .map(new HttpResultFuncNoList_has_obj())
                .map(new Func1<Obj, Boolean>() {

                    @Override
                    public Boolean call(Obj o) {
                        if (o != null) {
                            Shared.saveUserInfo(o);
                            //Log.i("lin", "----" + o.getIsReal());
                            Log.i("lin", "=====lin=====>   保存的用户姓名 3333" + o.getName());
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }

    /**
     * 用验证码登录的接口
     */
    public void login_usecode(HttpResultListener<Boolean> subscriber, final String tel, final String code) {
        mApi.login_usecode(tel, code)
                .map(new HttpResultFuncNoList_has_obj())
                .map(new Func1<Obj, Boolean>() {
                    @Override
                    public Boolean call(Obj o) {
                        if (o != null) {
                            Shared.saveUserInfo(o);
                            Log.i("lin", "=====lin=====>   保存的用户姓名 3333" + o.getName());
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }


    /**
     * 发布悬赏令
     */
    public void wanted(HttpResultListener<String> subscriber, String productName, String price
            , String num, String address, int deliveryTime, int pack
            , String deliveryArea, String requirements, String picture, Double total, String createType) {
        mApi.wanted(productName, price, num, address, deliveryTime, pack, deliveryArea, requirements, picture, total, createType)
                .map(new HttpResultFuncNoList_has_obj())
                .map(new Func1<Obj, String>() {

                    @Override public String call(Obj Obj) {
                        return Obj.getWid();
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }
//    /**
//     * 注册的接口
//     */
//    public void register(HttpResultListener<Boolean> listener, final String tel, final String password, final String code) {
//        mApi.register(tel, password, code)
//                .map(new HttpResultFuncNoList_has_obj())
//                .map(new Func1<Obj, Boolean>() {
//
//                    @Override
//                    public Boolean call(Obj o) {
//                        if (o != null) {
//                            Shared.saveUserInfo(o);
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new HttpResultSubscriber<>(listener));
//    }

    /**
     * 获取用户所有地址
     */
    public void allAddress(HttpResultListener<List<AllAddress>> listener, final int noMain) {
        mApi.allAddress(noMain)
                .map(new HttpResultFunc<List<AllAddress>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 添加用户的收货地址
     */
    public void address(HttpResultListener<Boolean> listener, final String shipName, final String province
            , final String city, final String area, final String detail, final String phone, final String zip
            , final int defaultAddress) {
        mApi.address(shipName, province, city, area, detail, phone, zip, defaultAddress)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 获取明星界面
     */
    public void famous2(HttpResultListener<List<JourneyModel>> listener, int page) {
        mApi.famous2(page)
                .map(new HttpResultFunc<List<JourneyModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 发送行程的接口
     */
    public void route(HttpResultListener<Boolean> subscriber, final String travelDate, final String startPlace
            , final String travelPlace, final String mainCity, final String returnDate, final String perference
            , final String instruction) {
        mApi.route(travelDate, startPlace, travelPlace, mainCity, returnDate, perference, instruction)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.i("lin", "---lin--->    " + s);
                        if (s != null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }


    /**
     * 获取明星界面
     */
    public void route2(HttpResultListener<List<JourneyModel>> listener, String id) {
        mApi.route2(id)
                .map(new HttpResultFunc<List<JourneyModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取明星界面
     */
    public void sellerDetails(HttpResultListener<Obj> listener, String id) {
        mApi.sellerDetails(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取支付宝界面
     */
    public void wantedPay(HttpResultListener<String> listener, String id) {
        mApi.wantedPay(id)
                .map(new HttpResultFunPay())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取商品详情界面
     */
    public void wanted2(HttpResultListener<Obj> listener, String orderId) {
        mApi.wanted2(orderId)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取商品详情界面
     */
    public void meOrderDetails(HttpResultListener<Obj> listener, String id) {
        mApi.meOrderDetails(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取钱包余额
     */
    public void walltet(HttpResultListener<Obj> listener, String id) {
        mApi.wallet(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取钱包交易的详情
     */

    public void walletLog(HttpResultListener<List<WalletLogModel>> listener, String id) {
        mApi.walletLog(id)
                .map(new HttpResultFunc<List<WalletLogModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 查询行程（根据国家名）
     */

//    public void walletLog(HttpResultListener<List<WalletLogModel>> listener, String id) {
//        mApi.walletLog(id)
//                .map(new HttpResultFunc<List<WalletLogModel>>())
//                .subscribeOn(Schedulers.io())//在工作线程请求网络
//                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
//                .subscribe(new HttpResultSubscriber<>(listener));
//    }


    /**
     * 获取我的行程
     */

    public void meRoute(HttpResultListener<List<JourneyModel>> listener, String page, String rows) {
        mApi.meRoute(page, rows)
                .map(new HttpResultFunc<List<JourneyModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取我的悬赏令
     */

    public void meReward(HttpResultListener<List<FamousPageModel>> listener, String page, String rows) {
        mApi.meReward(page, rows)
                .map(new HttpResultFunc<List<FamousPageModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 修改姓名
     */

    public void updateName(HttpResultListener<Boolean> subscriber, String name) {
        mApi.updateName(name)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.i("lin", "---lin--->    " + s);
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }


    /**
     * 修改密码
     */

    public void updatePwd(HttpResultListener<Boolean> subscriber, String oldPwd, String newPwd) {
        mApi.updateLoginPwd(oldPwd, newPwd)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.i("lin", "---lin--->    " + s);
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }

    /**
     * 修改密码
     */

    public void updateHead(HttpResultListener<Boolean> subscriber, String head) {
        mApi.updateHead(head)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.i("lin", "---lin--->    " + s);
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }


    /**
     * 获取我的
     */
    public void purse(HttpResultListener<Obj> listener, String id) {
        mApi.purse(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 获取我的
     */
    public void weiXinquankuan(HttpResultListener<Obj> listener, String id) {
        mApi.weiXinquankuan(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 下单接口
     */
    public void place(HttpResultListener<Obj> listener, String wid) {
        mApi.place(wid)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 下单接口
     */
    public void certification(HttpResultListener<Boolean> subscriber, String realName, String carded, String cardedPicture, String passportPicture) {
        mApi.certification(realName, carded, cardedPicture, passportPicture)
                .map(new HttpResultFuncNoList())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        if (s.equals("ok")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(subscriber));
    }


    /**
     * 查询之后的悬赏令
     */

    public void q1(HttpResultListener<List<FamousPageModel>> listener, String page, String query, String field, String order) {
        mApi.q1(page, query, field, order)
                .map(new HttpResultFunc<List<FamousPageModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 我的订单的接口
     */
    public void meOrder(HttpResultListener<List<FamousPageModel>> listener, String page, String rows) {
        mApi.meOrder(page, rows)
                .map(new HttpResultFunc<List<FamousPageModel>>())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 押金支付获取签名
     */
    public void depositPay(HttpResultListener<String> listener, String orderId) {
        mApi.depositPay(orderId)
                .map(new HttpResultFunPay())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 微信支付悬赏令接口
     */
    public void weiXin_xuanShang(HttpResultListener<Obj> listener, String wid) {
        mApi.weiXin_xuanShang(wid)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 第一次设置支付密码
     */
    public void setPayPwd(HttpResultListener<Obj> listener, String newPassword) {
        mApi.setPayPwd(newPassword)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 第一次绑定支付宝号
     */
    public void bindingAli(HttpResultListener<Obj> listener, String newPassword) {
        mApi.bindingAli(newPassword)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 重新绑定支付宝号
     */
    public void bindingAliNew(HttpResultListener<Obj> listener, String newPassword) {
        mApi.bindingAliNew(newPassword)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 提现接口
     */
    public void withdraw(HttpResultListener<String> listener, String sum, String pwd) {
        mApi.withdraw(sum, pwd)
                .map(new HttpResultFunPay())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 新的提现接口  16-11-02
     */
    public void newWithdraw(HttpResultListener<String> listener, String name, String pwd, String sum, String alipay) {
        mApi.newWithdraw(name, pwd, sum, alipay)
                .map(new HttpResultFunPay())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


    /**
     * 删除悬赏令
     */
    public void deleteReward(HttpResultListener<Obj> listener, String id) {
        mApi.deleteReward(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 删除旅游行程
     */
    public void deleteRoute(HttpResultListener<Obj> listener, String id) {
        mApi.deleteRoute(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 确认收货
     */
    public void sureShouHuo(HttpResultListener<Obj> listener, String id) {
        mApi.sureShouHuo(id)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }

    /**
     * 确认收货
     */
    public void sureFaHuo(HttpResultListener<Obj> listener, String orderOverId, String airTicket, String expressNumber, String orderId) {
        mApi.sureFaHuo(orderOverId, airTicket, expressNumber, orderId)
                .map(new HttpResultFuncNoList_has_obj())
                .subscribeOn(Schedulers.io())//在工作线程请求网络
                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
                .subscribe(new HttpResultSubscriber<>(listener));
    }


//    /**
//     * 获取支付宝界面
//     */
//    public void wantedPay(HttpResultListener<String> listener, String id) {
//        mApi.wantedPay(id)
//                .map(new HttpResultFunPay())
//                .subscribeOn(Schedulers.io())//在工作线程请求网络
//                .observeOn(AndroidSchedulers.mainThread())//在主线程处理结果
//                .subscribe(new HttpResultSubscriber<>(listener));
//    }


    private static class HttpResultSubscriber<T> extends Subscriber<T> {
        private HttpResultListener<T> mListener;

        public HttpResultSubscriber(HttpResultListener<T> listener) {
            mListener = listener;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (mListener != null) {
                mListener.onError(e);
            }
        }

        @Override
        public void onNext(T t) {
            if (mListener != null) {
                mListener.onSuccess(t);
            }
        }
    }


    /**
     * 对返回结果做统一处理,只有当结果码为 100 时,才返回正常,否则返回错误代码,不带list的
     */
    private class HttpResultFuncNoList implements Func1<ResponseModel_no_list, String> {

        @Override
        public String call(ResponseModel_no_list responseModel) {

            if (responseModel.getCode() == NetworkException.REQUEST_OK) {

                return responseModel.getMsg();

            } else {
                throw new NetworkException(responseModel.getCode());
            }

        }
    }

    /**
     * 对返回结果做统一处理,只有当结果码为 100 时,才返回正常,否则返回错误代码,不带list的
     */
    private class HttpResultFunPay implements Func1<PayModel, String> {

        @Override
        public String call(PayModel responseModel) {

            if (responseModel.getCode() == NetworkException.REQUEST_OK) {

                return responseModel.getObj();

            } else {
                throw new NetworkException(responseModel.getCode());
            }

        }
    }

    /**
     * 对返回结果做统一处理,只有当结果码为 100 时,才返回正常,否则返回错误
     */
    private class HttpResultFunc<T> implements Func1<ResponseModel<T>, T> {

        @Override
        public T call(ResponseModel<T> tResponseModel) {
            if (tResponseModel.getCode() == NetworkException.REQUEST_OK) {
                return tResponseModel.getResult();
            } else {
                throw new NetworkException(tResponseModel.getCode());
            }
        }
    }


    /**
     * 对返回结果做统一处理,只有当结果码为 100 时,才返回正常,否则返回错误,不带list的,具有对象的
     */
    private class HttpResultFuncNoList_has_obj implements Func1<ResponseModel_no_list, Obj> {

        @Override
        public Obj call(ResponseModel_no_list responseModel) {
            if (responseModel.getCode() == NetworkException.REQUEST_OK) {
                KLog.i("____lin____> + aaaaa");
                return responseModel.getObj();
            } else {
                Log.i("lin", "---lin--->  错误代码：  " + responseModel.getCode());
                throw new NetworkException(responseModel.getCode());
            }
        }
    }


}
