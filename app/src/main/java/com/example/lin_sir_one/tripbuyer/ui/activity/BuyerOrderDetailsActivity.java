package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.leanchat.CustomUserProvider;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKit;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKitUser;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.activity.LCIMConversationActivity;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.utils.LCIMConstants;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/5.买家订单的详情页面,
 */
public class BuyerOrderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_product_1) ImageView p1_iv;
    @BindView(R.id.iv_product_2) ImageView p2_iv;
    @BindView(R.id.iv_product_3) ImageView p3_iv;

    @BindView(R.id.tv_product_name_1) TextView p1_name;
    @BindView(R.id.tv_product_name_2) TextView p2_name;
    @BindView(R.id.tv_product_name_3) TextView p3_name;

    @BindView(R.id.tv_product_price_1) TextView p1_price;
    @BindView(R.id.tv_product_price_2) TextView p2_price;
    @BindView(R.id.tv_product_price_3) TextView p3_price;

    @BindView(R.id.tv_product_count_1) TextView p1_count;
    @BindView(R.id.tv_product_count_2) TextView p2_count;
    @BindView(R.id.tv_product_count_3) TextView p3_count;

    @BindView(R.id.tv_time_details) TextView time;
    @BindView(R.id.tv_pack_details) TextView pack;
    @BindView(R.id.tv_introduction_details) TextView introduction;

    @BindView(R.id.linear_2_order_details) LinearLayout l2;
    @BindView(R.id.linear_3_order_details) LinearLayout l3;

    @BindView(R.id.talk_order_details) TextView talk;

    @BindView(R.id.relativeLayout2) RelativeLayout rl;
    @BindView(R.id.details) LinearLayout details;

    @BindView(R.id.tv_1) TextView tv_1;
    @BindView(R.id.tv_2) TextView tv_2;
    @BindView(R.id.tv_3) TextView tv_3;
    @BindView(R.id.tv_4) TextView tv_4;
    @BindView(R.id.tv_5) TextView tv_5;
    @BindView(R.id.tv_6) TextView tv_6;

    @BindView(R.id.select_order) TextView receivedOrder;

    String t_2 = "";            //昵称
    String t_3 = "";            //手机号
    String t_5 = "";            //订单号
    String t_6 = "";            //运单号

    String uid = "";
    String head = "";
    String name = "";

    String orderUserId = "";
    Dialog makeSureDialog;
    double tottals = 0.0;
    String wid = null;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_details);
        ButterKnife.bind(this);


        Intent intent = getIntent();

        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {

                KLog.i("----ooo----成功>" + obj.getProductName());
                KLog.i("----ooo----成功>" + obj.getPicture());

                try {
                    orderUserId = obj.getUser().getUid();
                    wid = obj.getWid();
                    uid = obj.getUser().getUid();
                    if (uid.equals(Shared.getUserInfo().getUid()))
                        receivedOrder.setVisibility(View.INVISIBLE);
                    head = obj.getUser().getHead();
                    name = obj.getUser().getName();
                } catch (Exception e) {

                }


                try {
                    if (orderUserId.equals(Shared.getUserInfo().getUid())) {
                        talk.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    talk.setVisibility(View.GONE);
                    receivedOrder.setVisibility(View.GONE);
                }

                tottals = obj.getTotal();
                String productName = obj.getProductName();
                switch (productName.split(";").length) {
                    case 1:
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture(), p1_iv);
                        p1_name.setText(productName);
                        p1_price.setText("￥" + obj.getPrice());
                        p1_count.setText("x" + obj.getNum());
                        break;
                    case 2:
                        l2.setVisibility(View.VISIBLE);
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture().split(";")[0], p1_iv);
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture().split(";")[1], p2_iv);
                        p1_name.setText(productName.split(";")[0]);
                        p2_name.setText(productName.split(";")[1]);
                        p1_price.setText("￥" + obj.getPrice().split(";")[0]);
                        p2_price.setText("￥" + obj.getPrice().split(";")[1]);
                        p1_count.setText("x" + obj.getNum().split(";")[0]);
                        p2_count.setText("x" + obj.getNum().split(";")[1]);
                        break;
                    case 3:
                        l2.setVisibility(View.VISIBLE);
                        l3.setVisibility(View.VISIBLE);
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture().split(";")[0], p1_iv);
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture().split(";")[1], p2_iv);
                        ImageUtil.requestImg(BaseApplication.get().getAppContext(), obj.getPicture().split(";")[2], p3_iv);
                        p1_name.setText(productName.split(";")[0]);
                        p2_name.setText(productName.split(";")[1]);
                        p3_name.setText(productName.split(";")[2]);
                        p1_price.setText("￥" + obj.getPrice().split(";")[0]);
                        p2_price.setText("￥" + obj.getPrice().split(";")[1]);
                        p3_price.setText("￥" + obj.getPrice().split(";")[2]);
                        p1_count.setText("x" + obj.getNum().split(";")[0]);
                        p2_count.setText("x" + obj.getNum().split(";")[1]);
                        p3_count.setText("x" + obj.getNum().split(";")[2]);
                        break;
                }
                if (obj.getPack() == 1)
                    pack.setText("产品包装：需要包装");
                if (obj.getPack() == 0)
                    pack.setText("产品包装：可无包装");

                if (obj.getDeliveryTime() == 5)
                    time.setText("发货时间：五天内发货");
                if (obj.getDeliveryTime() == 10)
                    time.setText("发货时间：十天内发货");
                if (obj.getDeliveryTime() == 15)
                    time.setText("发货时间：十五天内发货");

                introduction.setText("特殊要求：" + obj.getRequirements());

            }

            @Override public void onError(Throwable e) {
                KLog.i("----ooo---->   返回值失败");
            }
        };

        KLog.i("----ooo---->" + intent.getExtras().getString("wid"));
        A.getA(3, 0, 1).wanted2(listener, intent.getExtras().getString("wid"));

        try {
            Intent intent2 = getIntent();
            if (intent2.getExtras().getString("who").equals("me")) {
                details.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }


//        try {
//            if (intent.getExtras().getString("xinXi").equals("dzf")) {
//                talk.setVisibility(View.INVISIBLE);
//                rl.setVisibility(View.INVISIBLE);
//            }
//
//            if (intent.getExtras().getString("xinXi").equals("yzf")) {
//                talk.setVisibility(View.INVISIBLE);
//                rl.setVisibility(View.INVISIBLE);
//            }
//
//            if (intent.getExtras().getString("xinXi").equals("jyz")) {
//                talk.setVisibility(View.INVISIBLE);
//                rl.setVisibility(View.INVISIBLE);
//            }
//
//            if (intent.getExtras().getString("xinXi").equals("jywc")) {
//                talk.setVisibility(View.INVISIBLE);
//                rl.setVisibility(View.INVISIBLE);
//            }
//        } catch (Exception e) {
//
//        }

        // 0:发生了异常     1：本人是买家     2:本人是卖家
        // 0:发生了异常     1：未发货  2：已发货  3：已完成  4：订单已取消
        // tv:  1.买家信息  2.用户昵称  3.用户手机号  4.订单与运单信息
        //      5.订单号    6.运单号
//
//        try {
//            KLog.i("----lin---->" + intent.getExtras().getString("w"));
//            if (intent.getExtras().getString("w").equals("11")) {   //买家，未发货
//                details.setVisibility(View.VISIBLE);
//                //tv
//
//            }
//
//            if (intent.getExtras().getString("w").equals("12")) {
//                details.setVisibility(View.VISIBLE);
//            }
//
//            if (intent.getExtras().getString("w").equals("13")) {
//                details.setVisibility(View.VISIBLE);
//            }
//
//            if (intent.getExtras().getString("w").equals("21")) {
//                details.setVisibility(View.VISIBLE);
//            }
//
//            if (intent.getExtras().getString("w").equals("22")) {
//                details.setVisibility(View.VISIBLE);
//            }
//
//            if (intent.getExtras().getString("w").equals("23")) {
//                details.setVisibility(View.VISIBLE);
//            }
//
//        } catch (Exception e) {
//
//        }


    }


    @OnClick(R.id.back_take_delivery)
    public void back() {
        finish();
    }

    @OnClick(R.id.talk_order_details)
    public void talk() {

        if (orderUserId.equals("")) {
            Toast.makeText(BuyerOrderDetailsActivity.this, "加载聊天界面失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("lin", "---linSir--->   111" + Shared.getUserInfo().getUid());
        Log.i("lin", "---linSir--->   222" + orderUserId);

        LCChatKit.getInstance().open(Shared.getUserInfo().getUid(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    finish();
                    Intent intent = new Intent(BuyerOrderDetailsActivity.this, LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, uid);
                    intent.putExtra(LCIMConstants.IMAGE_URL, head);
                    CustomUserProvider.getInstance().partUsers.add(new LCChatKitUser(uid, name, head));


                    startActivity(intent);
                } else {
                    Toast.makeText(BuyerOrderDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick(R.id.select_order)
    public void select() {
        if (!uid.equals(Shared.getUserInfo().getUid()))
            showSetPayPwdDialog(this);


    }

    private void showSetPayPwdDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_sure_pay, null);
        builder.setView(mView);
        builder.setCancelable(true);
        makeSureDialog = builder.create();
        makeSureDialog.show();

        TextView sure = (TextView) mView.findViewById(R.id.pay_make_sure);
        TextView total = (TextView) mView.findViewById(R.id.totals_sure_pay);

        KLog.i("____lin____>" + tottals);
        tottals = tottals * 0.03;
        int aaa = 0;
        aaa = (int) tottals;
        if (aaa == 0)
            aaa = 1;
        final String temp = String.valueOf(aaa);

        total.setText(String.valueOf(aaa) + "元");

        sure.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                Toast.makeText(BuyerOrderDetailsActivity.this, "点击了", Toast.LENGTH_SHORT).show();

                Log.i("lin","---lin's log--->   点击了  确认支付");

                if (Shared.getUserInfo().getIsReal().equals("1")) {
                    //Toast.makeText(BuyerOrderDetailsActivity.this, "已认证" + Shared.getUserInfo().getIsReal(), Toast.LENGTH_SHORT).show();
                    HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                        @Override public void onSuccess(Obj obj) {
                            KLog.i("____lin____>  orderId" + obj.getOrderId());
                            Intent intent = new Intent(BuyerOrderDetailsActivity.this, OnlinePayActivity.class);
                            intent.putExtra("yajin", temp);
                            intent.putExtra("orderId", obj.getOrderId());
                            startActivity(intent);
                        }

                        @Override public void onError(Throwable e) {
                            Log.i("lin", "____lin____>  no" + e.toString());
                        }
                    };

                    Log.i("lin", "____lin____>  wid : " + wid);
                    A.getA(5, 0, 1).place(listener, wid);
                } else {
                    Toast.makeText(BuyerOrderDetailsActivity.this, "实名认证后才可以接单", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BuyerOrderDetailsActivity.this, AuthenticationActivity.class);
                    startActivity(intent);

                }


//                Intent intent = new Intent(BuyerOrderDetailsActivity.this, OnlinePayActivity.class);
//                startActivity(intent);
            }
        });

    }

}
