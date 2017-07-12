package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * 思路：先确定，自己是买家[1]还是卖家[2]（2种）
 * 再确定，待发货[1](其它)，已发货[2]（jyz），已完成[3](jywc),交易取消[4]（qx）  四种状态
 */
public class MeOrderDetailsActivity extends AppCompatActivity {

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
    @BindView(R.id.tv_2) TextView buyer_name;
    @BindView(R.id.tv_3) TextView buyer_phone;
    @BindView(R.id.tv_7) TextView seller_name;
    @BindView(R.id.tv_8) TextView seller_phone;
    @BindView(R.id.tv_4) TextView tv_4;
    @BindView(R.id.tv_5) TextView ding_dan_hao;
    @BindView(R.id.tv_6) TextView yun_dan_hao;

    @BindView(R.id.select_order) TextView receivedOrder;

    @BindView(R.id._ding_dan_hao_e) EditText e_ding_dan_hao;
    @BindView(R.id._yun_dan_hao_e) EditText e_yun_dan_hao;
    @BindView(R.id.upload_air_ticket) Button uploadTicket;


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

    int people = 0;
    int style = 0;

    String orderOverId = "";
    String airTicket = "";


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_order_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {

                KLog.i("----ooo----成功>" + obj.getProductName());
                KLog.i("----ooo----成功>" + obj.getPicture());

                orderOverId = obj.getOrderOver().getOrderOverId();

                ding_dan_hao.setText("订单号：" + obj.getOrderOver().getOrderId());

                buyer_name.setText("买家昵称：" + obj.getBuyUser().getName());
                buyer_phone.setText("买家手机号：" + "后台暂时没给而我们");
                seller_name.setText("卖家昵称：" + obj.getSellerUser().getName());
                seller_phone.setText("买家手机号：" + "后台暂时没给而我们");

                wid = obj.getWid();
                if (Shared.getUserInfo().getUid().equals(obj.getSellerUser().getUid())) {
                    uid = obj.getSellerUser().getUid();
                    head = obj.getSellerUser().getHead();
                    name = obj.getSellerUser().getName();
                    people = 2;
                } else {
                    uid = obj.getBuyUser().getUid();
                    head = obj.getBuyUser().getHead();
                    name = obj.getBuyUser().getName();
                    people = 1;
                }

                if (obj.getStatus().equals("jyz")) {
                    style = 2;
                } else if (obj.getStatus().equals("jywc")) {
                    style = 3;
                } else if (obj.getStatus().equals("qx")) {
                    style = 4;
                } else if (obj.getStatus().equals("yzjpxx")) {
                    style = 5;
                } else {
                    style = 1;
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


                //待发货[1](其它)，已发货[2]（jyz），已完成[3](jywc),交易取消[4]（qx）  四种状态
                if (people == 1 && style == 1) {//1.买家   2.卖家
                    receivedOrder.setText("等待卖家发货");
                } else if (people == 1 && style == 2) {
                    receivedOrder.setText("确认收货");

                } else if (people == 1 && style == 3) {

                    receivedOrder.setText("交易已完成");
                } else if (people == 1 && style == 4) {
                    receivedOrder.setText("交易已取消");
                } else if (people == 1 && style == 5) {
                    receivedOrder.setText("后台验证机票中");
                    uploadTicket.setVisibility(View.VISIBLE);
                } else if (people == 2 && style == 1) {
                    receivedOrder.setText("发货");
                    uploadTicket.setVisibility(View.VISIBLE);

                } else if (people == 2 && style == 2) {
                    receivedOrder.setText("等待买家确认收货");

                } else if (people == 2 && style == 3) {
                    receivedOrder.setText("交易已完成");

                } else if (people == 2 && style == 4) {
                    receivedOrder.setText("交易已取消");
                } else if (people == 2 && style == 5) {
                    receivedOrder.setText("后台验证机票中");
                    uploadTicket.setVisibility(View.VISIBLE);
                }


            }

            @Override public void onError(Throwable e) {
                KLog.i("----ooo---->   返回值失败" + e.toString());
            }
        };

        KLog.i("----ooo---->" + intent.getExtras().getString("orderId"));
        A.getA(4, 0, 1).meOrderDetails(listener, intent.getExtras().getString("orderId"));
        details.setVisibility(View.VISIBLE);


    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            airTicket = data.getExtras().getString("air_ticket");
        }
    }

    @OnClick(R.id.back_take_delivery)
    public void back() {
        finish();
    }

    @OnClick(R.id.talk_order_details)
    public void talk() {

        if (uid.equals("")) {
            Toast.makeText(MeOrderDetailsActivity.this, "加载聊天界面失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("lin", "---linSir--->   111" + Shared.getUserInfo().getUid());
        Log.i("lin", "---linSir--->   222" + orderUserId);

        LCChatKit.getInstance().open(Shared.getUserInfo().getUid(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    finish();
                    Intent intent = new Intent(MeOrderDetailsActivity.this, LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, uid);
                    intent.putExtra(LCIMConstants.IMAGE_URL, head);
                    CustomUserProvider.getInstance().partUsers.add(new LCChatKitUser(uid, name, head));


                    startActivity(intent);
                } else {
                    Toast.makeText(MeOrderDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick(R.id.select_order)
    public void select() {


        if (people == 1 && style == 2) {//确认收货
            chooseIsShouHuo();
        } else if (people == 2 && style == 1) {//确认发货
            chooseIsFaHuo();
        }
    }

    /**
     * 是否确认发货
     */
    private void chooseIsFaHuo() {
        new android.app.AlertDialog.Builder(MeOrderDetailsActivity.this)
                .setTitle(getResources().getString(R.string.isTrue))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (airTicket.equals("")) {
                            Toast.makeText(MeOrderDetailsActivity.this, "请上传登机牌后，再发货", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if (e_yun_dan_hao.getText().toString().equals("")) {
                            Toast.makeText(MeOrderDetailsActivity.this, "请填写运单号后，再发货", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                            @Override public void onSuccess(Obj obj) {
                                Toast.makeText(MeOrderDetailsActivity.this, "发货成功", Toast.LENGTH_SHORT).show();


                            }

                            @Override public void onError(Throwable e) {
                                Toast.makeText(MeOrderDetailsActivity.this, "发货失败" + e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        };
                        Intent intent = getIntent();
                        A.getA(4, 0, 1).sureFaHuo(listener, orderOverId, airTicket, e_yun_dan_hao.getText().toString(), intent.getExtras().getString("orderId"));

                    }
                })
                .setNegativeButton(getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }

    /**
     * 是否确认收货
     */
    private void chooseIsShouHuo() {
        new android.app.AlertDialog.Builder(MeOrderDetailsActivity.this)
                .setTitle(getResources().getString(R.string.isTrue))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                            @Override public void onSuccess(Obj obj) {
                                Toast.makeText(MeOrderDetailsActivity.this, "确认收货成功", Toast.LENGTH_SHORT).show();

                            }

                            @Override public void onError(Throwable e) {
                                Toast.makeText(MeOrderDetailsActivity.this, "确认收货失败" + e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        };
                        Intent intent = getIntent();
                        A.getA(4, 0, 1).sureShouHuo(listener, intent.getExtras().getString("orderId"));

                    }
                })
                .setNegativeButton(getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }

    @OnClick(R.id.upload_air_ticket)
    public void uploadAirTicket() {

        Toast.makeText(MeOrderDetailsActivity.this, "-----》", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MeOrderDetailsActivity.this, UploadAirTicketActivity.class);
        startActivityForResult(intent, 100);


    }

}
