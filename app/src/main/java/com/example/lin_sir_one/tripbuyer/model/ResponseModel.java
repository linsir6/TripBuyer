package com.example.lin_sir_one.tripbuyer.model;

/**
 * Created by lin_sir on 7/1/16.结果中带有list的，网络请求返回结果
 */
public class ResponseModel<T> {
    private int code;
    private String message;

    private T obj;

    public ResponseModel(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.obj = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return obj;
    }

    public void setResult(T result) {
        this.obj = result;
    }


}
