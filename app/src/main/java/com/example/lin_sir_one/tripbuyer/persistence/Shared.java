package com.example.lin_sir_one.tripbuyer.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.LoginModel;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.model.UserInfoModel;

/**
 * Created by lin_sir on 2016/7/7.sharedPreference 工具类
 * 注意这里的所有写操作都是异步方法,不保证立即执行,因此建议不要在执行写操作后立即执行读操作
 */
public class Shared {

    private static SharedPreferences mDefaultShared;
    private SharedPreferences.Editor mEditor;

    private static SharedPreferences mLoginShared;
    private static SharedPreferences mUserInfoShared;
    private static SharedPreferences mCreditShared;

    /**
     * 默认的 Shared,用于存储一些配置信息
     */
    public static SharedPreferences getDefaultShared() {
        if (mDefaultShared == null) {
            mDefaultShared = BaseApplication.get().getAppContext().getSharedPreferences("SHARED_CACHE_DATA", Context.MODE_PRIVATE);
        }
        return mDefaultShared;
    }

    /**
     * 存储登录信息的 Shared
     */
    private static SharedPreferences getLoginShared() {
        if (mLoginShared == null) {
            mLoginShared = BaseApplication.get().getAppContext().getSharedPreferences("LOGIN_CACHE_DATA", Context.MODE_PRIVATE);
        }
        return mLoginShared;
    }

    /**
     * 存储用户信息的 Shared
     */
    private static SharedPreferences getUserInfoShared() {
        if (mUserInfoShared == null) {
            mUserInfoShared = BaseApplication.get().getAppContext().getSharedPreferences("USER_CACHE_DATA", Context.MODE_PRIVATE);
        }
        return mUserInfoShared;
    }

    /**
     * 保存用户登录信息
     *
     * @param uid   学号
     * @param pwd   密码
     * @param token 令牌
     *              TODO 考虑要不要做密码的加密存储
     */
    public static void saveLoginInfo(String uid, String pwd, String token) {

        SharedPreferences.Editor mEditor = getLoginShared().edit();

        mEditor.putString("uid", uid);
        mEditor.putString("pwd", pwd);
        mEditor.putString("token", token);
        mEditor.apply();
    }

    /**
     * 获取用户登录信息
     *
     * @return 如果用户没有登录或者因为其他原因导致没有用户登录数据, 返回 null
     */
    public static LoginModel getLoginInfo() {
        SharedPreferences loginShared = getLoginShared();

        String head = loginShared.getString("head", null);
        String name = loginShared.getString("name", null);
        String tel = loginShared.getString("tel", null);
        String uid = loginShared.getString("uid", null);

        if (head == null || name == null || tel == null || uid == null) {
            return null;
        }
        return new LoginModel(head, name, tel, uid);
    }

    /**
     * 清除用户登录信息
     */
    public static void clearLoginInfo() {
        SharedPreferences.Editor mEditor = getLoginShared().edit();
        mEditor.clear();
        mEditor.apply();
    }

    /**
     * 保存用户信息
     */
    public static void saveUserInfo(Obj user) {
        SharedPreferences.Editor mEditor = getUserInfoShared().edit();

        mEditor.putString("uid", user.getUid());
        mEditor.putString("name", user.getName());
        Log.i("lin", "=====lin=====>   保存的用户姓名" + user.getName());
        mEditor.putString("head", user.getHead());
        mEditor.putString("isReal", user.getIsReal());
        mEditor.apply();
    }

    /**
     * 获取用户信息
     *
     * @return null if not login,else user info entity
     */
    public static UserInfoModel getUserInfo() {

        SharedPreferences userInfoShared = getUserInfoShared();

        String uid = userInfoShared.getString("uid", "0");
        String name = userInfoShared.getString("name", null);
        String head = userInfoShared.getString("head", null);
        String isReal = userInfoShared.getString("isReal", null);

        if (uid == null || name == null || head == null || isReal == null) {
            return null;
        }
        return new UserInfoModel(uid, name, head, isReal);
    }

    /**
     * 清理用户信息
     */
    public static void clearUserInfo() {
        SharedPreferences.Editor mEditor = getUserInfoShared().edit();
        mEditor.clear();
        mEditor.apply();
    }

    /**
     * 保存上次请求的时间戳
     */
    public static void saveCookie(String cookie) {
        SharedPreferences.Editor mEditor = getDefaultShared().edit();
        mEditor.putString("Set-Cookie", cookie);
        mEditor.apply();
    }

    /**
     * 获取上次请求的时间戳
     */
    public static String getCookie() {
        SharedPreferences defaultShared = getDefaultShared();
        return defaultShared.getString("Set-Cookie", null);
    }

    /**
     * 保存上次请求的时间戳
     */
    public static void saveTel(String tel) {
        SharedPreferences.Editor mEditor = getDefaultShared().edit();
        mEditor.putString("tel_", tel);
        mEditor.apply();
    }

    /**
     * 获取上次请求的时间戳
     */
    public static String getTel() {
        SharedPreferences defaultShared = getDefaultShared();
        return defaultShared.getString("tel_", "0");
    }


    /**
     * 特殊要求界面的缓存
     */
    public static void saveSpecial(String cookie) {
        SharedPreferences.Editor mEditor = getDefaultShared().edit();
        mEditor.putString("special", cookie);
        mEditor.apply();
    }

    /**
     * 获取特殊界面的缓存
     */
    public static String getSpecial() {
        SharedPreferences defaultShared = getDefaultShared();
        return defaultShared.getString("special", "1;1; ");
    }

    /**
     * 特殊要求界面的缓存
     */
    public static void saveHotBoomLike(String hotBoomLike) {
        SharedPreferences.Editor mEditor = getDefaultShared().edit();
        mEditor.putString("hotBoomLike", hotBoomLike);
        mEditor.apply();
    }

    /**
     * 获取特殊界面的缓存
     */
    public static String getHotBoomLike() {
        SharedPreferences defaultShared = getDefaultShared();
        return defaultShared.getString("hotBoomLike", "1; ");
    }

    /**
     * 特殊要求界面的缓存
     */
    public static void saveIsFirst(String hotBoomLike) {
        SharedPreferences.Editor mEditor = getDefaultShared().edit();
        mEditor.putString("first", hotBoomLike);
        mEditor.apply();
    }

    /**
     * 获取特殊界面的缓存
     */
    public static String getIsFirst() {
        SharedPreferences defaultShared = getDefaultShared();
        return defaultShared.getString("first", "0");
    }









    public void saveString(String key, String value) {
        mEditor = mDefaultShared.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void saveInt(String key, int value) {
        mEditor = mDefaultShared.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public void saveBoolean(String key, Boolean value) {
        mEditor = mDefaultShared.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public void saveFloat(String key, float value) {
        mEditor = mDefaultShared.edit();
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public String getString(String key) {
        return mDefaultShared.getString(key, "");
    }

    public int getInt(String key) {
        return mDefaultShared.getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return mDefaultShared.getInt(key, def);
    }

    public Boolean getBoolean(String key) {
        return mDefaultShared.getBoolean(key, false);
    }

    public float getFloat(String key) {
        return mDefaultShared.getFloat(key, 0);
    }

}
