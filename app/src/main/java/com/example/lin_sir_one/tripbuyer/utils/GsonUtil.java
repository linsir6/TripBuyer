package com.example.lin_sir_one.tripbuyer.utils;

import android.util.Log;

import com.example.lin_sir_one.tripbuyer.model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by tc on 6 /21/16. Gson 解析工具类
 */
public class GsonUtil {


    private static Gson mGson;
    private static GsonUtil mInstance;

    public GsonUtil() {
        mGson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    public static GsonUtil getInstance() {
        if (mInstance == null) {
            mInstance = new GsonUtil();
        }
        return mInstance;
    }

    public Gson getGson() {
        return mGson;
    }

    public ResponseModel parseResponse(ResponseBody body){
        String data = null;
        try {
            JSONObject jo = new JSONObject(body.string());
            Log.i("TAG", "--tc-->json data: " + jo.toString());
            data = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mGson.fromJson(data, new TypeToken<ResponseModel>() {}.getType());
    }
}
