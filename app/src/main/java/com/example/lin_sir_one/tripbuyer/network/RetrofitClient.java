package com.example.lin_sir_one.tripbuyer.network;

import android.content.Context;

import com.example.lin_sir_one.tripbuyer.app.Constant;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lin_sir on 2016/8/3.网络操作的请求，决定采取哪种方式
 */
public class RetrofitClient {
    /**
     * 1.base Url    2.seller Url    3.buyer Url    4.wallet   5.payUrl
     */
    public static Retrofit getClient(Context context, int baseUrl, int intercept, int add) {

        if (baseUrl == 1 && intercept == 0 && add == 0)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    //.client(httpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();

        if (baseUrl == 3 && intercept == 0 && add == 0)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_BUY_URL)
                    //.client(httpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();

        if (baseUrl == 1 && intercept == 1 && add == 0)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(httpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();

        if (baseUrl == 3 && intercept == 0 && add == 1)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_BUY_URL)
                    .client(httpClient2(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();


        if (baseUrl == 1 && intercept == 0 && add == 1)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(httpClient2(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();


        if (baseUrl == 2 && intercept == 0 && add == 1)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_SELL_URL)
                    .client(httpClient2(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();


        if (baseUrl == 4 && intercept == 0 && add == 1)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_WALLET_URL)
                    .client(httpClient2(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();


        if (baseUrl == 5 && intercept == 0 && add == 1)
            return new Retrofit.Builder()
                    .baseUrl(Constant.BASE_WALLET_URL)
                    .client(httpClient2(context))
                    .addConverterFactory(GsonConverterFactory.create())//Gson 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// RxJava 适配器
                    .build();

        return null;
    }


    /**
     * 用来拦截响应头
     */
    private static OkHttpClient httpClient(Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new ResponseInterceptor(context))
                .build();
    }


    /**
     * 添加请求头
     */
    private static OkHttpClient httpClient2(Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(requestHeaderInterceptor)
                .build();
    }


    /**
     * 拦截请求头,添加必要的请求头信息
     */
    public static Interceptor requestHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String cookie = Shared.getCookie();
            if (cookie == null) {
                cookie = "111";
            }
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Cookie", cookie)
                    .method(original.method(), original.body())
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    };

    /**
     * 抓取到响应头中的cookie
     */
    private static class ResponseInterceptor implements Interceptor {
        private Context context;

        public ResponseInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Response response = chain.proceed(chain.request());

            String a = response.header("Set-Cookie");
            if (a == null) {
                KLog.d("----lin----> 他没有cookie");
            } else {
                String b = a.split(";")[0];
                KLog.d("-----lin----> 登陆成功+他有cookie" + a);
                Shared.saveCookie(b);
            }
            return response;
        }
    }

}
