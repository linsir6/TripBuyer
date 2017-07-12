package com.example.lin_sir_one.tripbuyer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lin_sir_one.tripbuyer.app.BaseApplication;


/**
 * Created by tc on 6/21/16. 网络状态工具类
 */
public class NetworkUtil {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;


    /**
     * 获取当前网络类型
     *
     * @return -1:没有网络, 0:移动网络, 1:wifi网络
     */
    public static int getNetworkType() {
        int status = TYPE_NONE;
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.get().getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return status;
        }
        int mType = netInfo.getType();
        if (mType == ConnectivityManager.TYPE_MOBILE) {
            status = TYPE_MOBILE;
        } else if (mType == ConnectivityManager.TYPE_WIFI) {
            status = TYPE_WIFI;
        }
        return status;
    }



    /**
     * 当前是否有网络连接
     *
     * @return true 有网络(包括wifi和移动网络),false 没有网络
     */
    public static boolean hasNetwork() {

        return getNetworkType() != TYPE_NONE;
    }


}
