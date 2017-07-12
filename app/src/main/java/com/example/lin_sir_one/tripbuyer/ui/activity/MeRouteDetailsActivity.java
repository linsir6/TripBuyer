package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.customview.FlowLayout;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.utils.DateUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/30.我的行程详情界面,
 */
public class MeRouteDetailsActivity extends AppCompatActivity {

    @BindView(R.id.rel_address_details) RelativeLayout rl;
    @BindView(R.id.customView) FlowLayout mFlowLayout;              //标签布局第一行
    @BindView(R.id.flowLayout2) FlowLayout mFlowLayout2;            //标签布局下面的行

    @BindView(R.id.iv_user_details) ImageView userInfo;             //用户头像
    @BindView(R.id.user_name_details) TextView userName;            //用户姓名
    @BindView(R.id.country_1_details) TextView country1;            //国家1
    @BindView(R.id.country_2_details) TextView country2;          //国家2
    @BindView(R.id.country_3_details) TextView country3;          //国家3
    @BindView(R.id.flag1_details) ImageView flag1;                   //国旗1
    @BindView(R.id.flag2_details) ImageView flag2;                 //国旗2
    @BindView(R.id.flag3_details) ImageView flag3;                 //国旗3
    @BindView(R.id.begin_time_details) TextView Time;               //时间
    @BindView(R.id.begin_where_details) TextView where;             //从哪里出发
    @BindView(R.id.special_request_details) TextView request;       //特殊要求

    @BindView(R.id.message_details) TextView delete;

    private HttpResultListener<Obj> listener;
    String uid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_route_details);
        ButterKnife.bind(this);


        final ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);

        listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {
                Toast.makeText(MeRouteDetailsActivity.this, "成功", Toast.LENGTH_SHORT).show();
                KLog.i("----lin---->  successed");
                //     try {

                uid = obj.getUser().getUid();

                KLog.i("____lin____> 对方的uid" + uid);
                KLog.i("____lin____> 我自己的uid" + Shared.getUserInfo().getUid());


                String begin = DateUtil.longToDate(obj.getTravelDate());
                String end = DateUtil.longToDate(obj.getReturnDate());
                Time.setText("出行时间：" + begin + " - " + end);


                KLog.i("____lin____>" + "成功" + obj.getUser().getName());
                userName.setText(obj.getUser().getName());
                KLog.i("----lin---->  111111111111111111111111111111");
                ImageUtil.requestCircleImg(BaseApplication.get().getAppContext(), obj.getUser().getHead(), userInfo);


                KLog.i("----lin---->  222222222222222222222222222222" + obj.getUser().getHead());
                //request.setText("特殊要求：" + obj.getInstruction());
                KLog.i("----lin---->  3333333333333333333333333333333");
                where.setText("出发地点：" + obj.getStartPlace());
                KLog.i("----lin---->  4444444444444444444444444444444");
                try {
                    if (obj.getPerference().length() == 0) {

                    } else if (obj.getPerference().length() == 4) {
                        String text = obj.getPerference();
                        KLog.i("----lin----> text" + text);
                        text = text.replaceAll("10", "地方特产");
                        text = text.replaceAll("11", "零食饮品");
                        text = text.replaceAll("12", "工艺产品");
                        text = text.replaceAll("1", "美容护肤");
                        text = text.replaceAll("2", "母婴产品");
                        text = text.replaceAll("3", "数码家电");
                        text = text.replaceAll("4", "品牌服饰");
                        text = text.replaceAll("5", "图书音像");
                        text = text.replaceAll("6", "生活百货");
                        text = text.replaceAll("7", "动漫周边");
                        text = text.replaceAll("8", "医疗保健");
                        text = text.replaceAll("9", "箱包鞋帽");
                        KLog.i("----lin----> text" + text);
                        String mNames[] = {text};
                    } else {
                        String text = obj.getPerference();
                        KLog.i("----lin----> text" + text);
                        text = text.replaceAll("10", "地方特产");
                        text = text.replaceAll("11", "零食饮品");
                        text = text.replaceAll("12", "工艺产品");
                        text = text.replaceAll("1", "美容护肤");
                        text = text.replaceAll("2", "母婴产品");
                        text = text.replaceAll("3", "数码家电");
                        text = text.replaceAll("4", "品牌服饰");
                        text = text.replaceAll("5", "图书音像");
                        text = text.replaceAll("6", "生活百货");
                        text = text.replaceAll("7", "动漫周边");
                        text = text.replaceAll("8", "医疗保健");
                        text = text.replaceAll("9", "箱包鞋帽");
                        KLog.i("----lin----> text" + text);
                        String mNames[] = text.split(";");
                        KLog.i("----lin---->  5555555555555555555555555555555");
                        // String a = "母婴用品;母婴用品;母婴用品;母婴用品;母婴用品;母婴用品;母婴用品;母婴用品";
                        //String mNames[] = a.split(";");
                        mFlowLayout2.setVerticalSpacing(10);
                        KLog.i("----lin---->  6666666666666666666666666666666");
                        for (int i = 0; i < mNames.length; i++) {
                            TextView view = new TextView(BaseApplication.get().getAppContext());
                            view.setText(mNames[i]);
                            view.setTextColor(0xffff9800);
                            view.setTextSize(12);
                            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_bg));
                            if (i < 3) {
                                mFlowLayout.addView(view, lp);
                                mFlowLayout.setHorizontalSpacing(10);
                                mFlowLayout.setVerticalSpacing(10);
                            } else {
                                mFlowLayout2.addView(view, lp);
                                mFlowLayout2.setHorizontalSpacing(10);
                                mFlowLayout2.setVerticalSpacing(10);
                            }
                        }
                    }
                } catch (Exception e) {

                }
                KLog.i("----lin---->  77777777777777777777777777777");
                String country = obj.getTravelPlace();
                KLog.i("----lin---->  88888888888888888888888888888");
                KLog.i("----lin----> country" + country);
                switch (country.split(";").length) {
                    case 1:
                        KLog.i("----lin---->  999999999999999999999999");
                        country1.setText(country.split(";")[0]);
                        country2.setVisibility(View.GONE);
                        country3.setVisibility(View.GONE);
                        //flag1.setImageResource();
                        flag2.setVisibility(View.GONE);
                        flag3.setVisibility(View.GONE);
                        break;
                    case 2:
                        KLog.i("----lin---->  10101010101010101010100101010");
                        country1.setText(country.split(";")[0]);
                        country2.setText(country.split(";")[1]);
                        country3.setVisibility(View.GONE);
                        //flag1.setImageResource();
                        //flag2.setImageResource();
                        flag3.setVisibility(View.GONE);
                        break;
                    case 3:
                        KLog.i("----lin---->  11,11,11,11,11,11,11,11,11,11,11");
                        country1.setText(country.split(";")[0]);
                        country2.setText(country.split(";")[1]);
                        country3.setText(country.split(";")[2]);
                        //flag1.setImageResource();
                        //flag2.setImageResource();
                        //flag3.setVisibility(View.GONE);
                        break;
                }

                request.setText("特殊要求：" + obj.getInstruction());
                // } catch (Exception e) {
                //   KLog.i("----lin----> fa sheng le yi chang" );
                //}
            }

            @Override public void onError(Throwable e) {
                KLog.i("----lin---->  nonononononononononononono");
            }
        };


        //ApiService6.getInstance().route2(listener, "1");
        Intent intent = getIntent();
        A.getA(2, 0, 1).sellerDetails(listener, intent.getExtras().getString("rid"));
        KLog.i("----lin---->  rid" + intent.getExtras().getString("rid"));

        try {
            if (intent.getExtras().getString("xinXi").equals("yiChuXing")) {
                delete.setVisibility(View.INVISIBLE);
            }

            if (intent.getExtras().getString("xinXi").equals("weiChuXing")) {
            }
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.back_details)
    public void back() {
        finish();
    }

    @OnClick(R.id.down)
    public void down() {
        if (mFlowLayout2.getVisibility() == View.VISIBLE) {
            mFlowLayout2.setVisibility(View.GONE);
            KLog.i("---lin---> ok1111");
            return;
        }


        if (mFlowLayout2.getVisibility() == View.GONE) {
            mFlowLayout2.setVisibility(View.VISIBLE);
            KLog.i("---lin---> ok2222");
        }


        KLog.i("---lin---> ok");
    }


    @OnClick(R.id.message_details)
    public void message() {
        chooseImgDialog();
    }

    /**
     * 选择确认还是取消
     */
    private void chooseImgDialog() {
        new android.app.AlertDialog.Builder(MeRouteDetailsActivity.this)
                .setTitle(getResources().getString(R.string.isTrue))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                            @Override public void onSuccess(Obj obj) {

                            }

                            @Override public void onError(Throwable e) {

                            }
                        };
                        Intent intent = getIntent();
                        A.getA(4, 0, 1).deleteRoute(listener, intent.getExtras().getString("rid"));

                    }
                })
                .setNegativeButton(getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }


}