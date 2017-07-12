package com.example.lin_sir_one.tripbuyer.persistence;

import android.content.Context;

import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.LoginModel;
import com.example.lin_sir_one.tripbuyer.model.UserInfoModel;

/**
 * Created by lin_sir on 2016/7/7.user类，用于用户管理
 */
public class User {

    private Context context;
    private static User mInstance;

    //--------存在内存泄漏的写法------------------------------
//    private User(Context context) {
//        this.context = context;
//    }
//
//    public static User getCurrentUser(Context context) {
//        if (mInstance == null) {
//            mInstance = new User(context);
//        }
//        return mInstance;
//    }
    //-----------------------------------------------------

    private User() {
        this.context = BaseApplication.get().getAppContext();
    }

    public static User currentUser() {
        if (mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
    }

    /**
     * 判断当前用户是否登录
     *
     * @return true if is login,false else
     */
    public boolean isLogin() {
        return Shared.getLoginInfo() != null;
    }

    public LoginModel getLoginInfo() {
        return Shared.getLoginInfo();
    }

    public UserInfoModel getUserInfo() {
        return Shared.getUserInfo();
    }

//    public CreditModel getCreditInfo() {
//        return Shared.getCreditInfo();
//    }

    public String getUid() {
        LoginModel login = getLoginInfo();
        return login == null ? null : login.getUid();
    }

    public String getToken() {
        LoginModel login = getLoginInfo();
        return login == null ? null : login.getToken();
    }

    public void logout() {
        Shared.clearLoginInfo();
        Shared.clearUserInfo();
//        Shared.clearCreditInfo();
    }
}

