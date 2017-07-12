package com.example.lin_sir_one.tripbuyer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);

        api = WXAPIFactory.createWXAPI(this, "wx0f4493dbeca4d2b2");
        api.registerApp(Constants.APP_ID);


        Intent intent = getIntent();
        String wid = intent.getExtras().getString("wid");
        HttpResultListener<Obj> obj = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {

            }

            @Override public void onError(Throwable e) {

            }
        };
        //A.getA(4, 0, 1).weiXin_xuanShang();


        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

                                        @Override public void onClick(View v) {

                                            Button payBtn = (Button) findViewById(R.id.appay_btn);
                                            payBtn.setEnabled(false);
                                            Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                                            try {

                                                String url = "http://139.129.221.22:8080/lxms-user/weiPay/wantedPay";
                                                Intent intent = getIntent();
                                                url = url + "?id=" + intent.getExtras().getString("wid");
                                                String cookie = "JSESSIONID=2362C814E09F771E850ED3E7B5268685";
                                                OkHttpUtils
                                                        .post()
                                                        .url(url)
                                                        .addHeader("Cookie", cookie)
                                                        .addParams("id", "200")
                                                        .build()
                                                        .execute(new StringCallback() {
                                                            @Override public void onError(Request request, Exception e) {
                                                                Log.i("lin", "----lin---->appId:  " + "on error");
                                                            }

                                                            @Override public void onResponse(String response) {
                                                                Log.i("lin", "----lin---->appId:  " + "on response" + response);


                                                                String[] result = response.split("\"");

                                                                Log.i("lin", "----lin---->appId:  " + result[11]);
                                                                Log.i("lin", "----lin---->mch_id:  " + result[15]);
                                                                Log.i("lin", "----lin---->nonce_str:  " + result[19]);
                                                                Log.i("lin", "----lin---->prepay_id:  " + result[23]);
                                                                Log.i("lin", "----lin---->sign:  " + result[29]);
                                                                Log.i("lin", "----lin---->timestamp:  " + result[33]);

                                                                PayReq req = new PayReq();
                                                                req.appId = "wx0f4493dbeca4d2b2";
                                                                req.partnerId = "1377137302";
                                                                req.prepayId = result[23];
                                                                req.nonceStr = result[19];
                                                                req.timeStamp = result[33];
                                                                req.packageValue = "Sign=WXPay";
                                                                req.sign = result[29];
                                                                req.extData = "app data"; // optional
                                                                api.sendReq(req);


                                                            }
                                                        });


                                                Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信


                                            } catch (Exception e) {
                                                Log.e("PAY_GET", "异常：" + e.getMessage());
                                                Toast.makeText(PayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            payBtn.setEnabled(true);
                                        }
                                    }

        );
        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener()

                                       {

                                           @Override
                                           public void onClick(View v) {
                                               boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                                               Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
                                           }
                                       }

        );
    }

}
