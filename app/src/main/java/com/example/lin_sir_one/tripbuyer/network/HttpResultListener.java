package com.example.lin_sir_one.tripbuyer.network;

/**
 * Created by lin_sir on 2016/7/7.网络接口的回调
 */
public interface HttpResultListener<T> {

    void onSuccess(T t);

    void onError(Throwable e);

}
