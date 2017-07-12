package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/5.买家订单的详情页面,
 */
public class MeRewardDetails extends AppCompatActivity {

    @BindView(R.id.iv_product_12) ImageView p1_iv;
    @BindView(R.id.iv_product_22) ImageView p2_iv;
    @BindView(R.id.iv_product_32) ImageView p3_iv;

    @BindView(R.id.tv_product_name_12) TextView p1_name;
    @BindView(R.id.tv_product_name_22) TextView p2_name;
    @BindView(R.id.tv_product_name_32) TextView p3_name;

    @BindView(R.id.tv_product_price_12) TextView p1_price;
    @BindView(R.id.tv_product_price_22) TextView p2_price;
    @BindView(R.id.tv_product_price_32) TextView p3_price;

    @BindView(R.id.tv_product_count_12) TextView p1_count;
    @BindView(R.id.tv_product_count_22) TextView p2_count;
    @BindView(R.id.tv_product_count_32) TextView p3_count;

    @BindView(R.id.tv_time_details22) TextView time;
    @BindView(R.id.tv_pack_details22) TextView pack;
    @BindView(R.id.tv_introduction_details22) TextView introduction;

    @BindView(R.id.linear_2_order_details2) LinearLayout l2;
    @BindView(R.id.linear_3_order_details2) LinearLayout l3;


    @BindView(R.id.relativeLayout22) RelativeLayout rl;
    @BindView(R.id.details) LinearLayout details;


    //    @BindView(R.id.select_order) TextView receivedOrder;
    @BindView(R.id.select_order22) TextView cancel;

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
        setContentView(R.layout.activity_me_reward_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent.getExtras().getString("xinXi").equals("yzf")) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.INVISIBLE);
        }

        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {

                KLog.i("----ooo----成功>" + obj.getProductName());
                KLog.i("----ooo----成功>" + obj.getPicture());


                orderUserId = obj.getUser().getUid();
                wid = obj.getWid();
                uid = obj.getUser().getUid();
                head = obj.getUser().getHead();
                name = obj.getUser().getName();


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

    }


    @OnClick(R.id.back_take_delivery2)
    public void back() {
        finish();
    }


    @OnClick(R.id.select_order22)
    public void select() {
        chooseImgDialog();

    }

    /**
     * 选择确认还是取消
     */
    private void chooseImgDialog() {
        new android.app.AlertDialog.Builder(MeRewardDetails.this)
                .setTitle(getResources().getString(R.string.text_choose_image))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                            @Override public void onSuccess(Obj obj) {
                                Toast.makeText(MeRewardDetails.this, "删除订单成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override public void onError(Throwable e) {
                                Toast.makeText(MeRewardDetails.this, "删除订单失败" + e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        };

                        A.getA(4, 0, 1).deleteReward(listener, wid);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }

}
