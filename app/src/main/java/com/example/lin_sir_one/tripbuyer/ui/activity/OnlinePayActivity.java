package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.lin_sir_one.tripbuyer.Pay.PayResult;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/25.在线支付界面
 */
public class OnlinePayActivity extends AppCompatActivity {

    @BindView(R.id.checkbox1) CheckBox zhiFuBao;
    @BindView(R.id.checkbox2) CheckBox weiXin;
    @BindView(R.id.order_id) TextView _orderId;
    @BindView(R.id.order_money) TextView _money;

    String orderId = "";
    String s = "";

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pay);
        ButterKnife.bind(this);
        zhiFuBao.setChecked(true);
        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderId");
        _money.setText(intent.getExtras().getString("yajin"));
        _orderId.setText(orderId);

        KLog.i("____lin____>  orderId " + orderId);

        HttpResultListener<String> listener = new HttpResultListener<String>() {
            @Override public void onSuccess(String obj) {
                KLog.i("____lin____>  onlinePay +++" + obj);
                s = obj;
            }

            @Override public void onError(Throwable e) {

            }
        };

        A.getA(5, 0, 1).depositPay(listener, orderId);
    }


    @OnClick(R.id.checkbox1)
    public void _zhiFuBao() {
        if (zhiFuBao.isChecked()) {
            weiXin.setChecked(false);
        }
        if (!zhiFuBao.isChecked()) {
            weiXin.setChecked(true);
        }
    }

    @OnClick(R.id.checkbox2)
    public void _weiXin() {
        if (weiXin.isChecked()) {
            zhiFuBao.setChecked(false);
        }
        if (!weiXin.isChecked()) {
            zhiFuBao.setChecked(true);
        }
    }

    @OnClick(R.id.make_sure_pay)
    public void make_sure_pay() {

        if (zhiFuBao.isChecked()) {


            final String ss = s;

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(OnlinePayActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(ss, true);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }

        if (weiXin.isChecked()) {

        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OnlinePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OnlinePayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OnlinePayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}

