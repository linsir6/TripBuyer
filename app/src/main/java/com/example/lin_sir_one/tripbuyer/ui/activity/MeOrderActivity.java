package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.adapter.MeOrderAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/5.我的订单,
 */

/**
 * Created by linSir on 16/8/5.我的订单,
 */
public class MeOrderActivity extends AppCompatActivity {

    @BindView(R.id.me_order_) RecyclerView mRV;
    @BindView(R.id.hint_me_order) TextView hint;

    private MeOrderAdapter meOrderAdapter;
    private List<FamousPageModel> mList = new ArrayList<>();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_order);

        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeOrderActivity.this);
            dialog2(builder);
        }

        ButterKnife.bind(this);
        meOrderAdapter = new MeOrderAdapter();
        mRV.setAdapter(meOrderAdapter);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));

        HttpResultListener<List<FamousPageModel>> listener = new HttpResultListener<List<FamousPageModel>>() {
            @Override public void onSuccess(List<FamousPageModel> famousPageModels) {

                mList = famousPageModels;
                if (famousPageModels.size() == 0)
                    hint.setVisibility(View.VISIBLE);

                Log.i("lin", "-----lin----->   cheng gong ding dan");
                meOrderAdapter.refresh(famousPageModels);

            }

            @Override public void onError(Throwable e) {
                hint.setVisibility(View.VISIBLE);
                Log.i("lin", "-----lin----->   shi bai " + e.toString());
            }
        };


        A.getA(5, 0, 1).meOrder(listener, "1", "200");


    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            Intent intent = new Intent(MeOrderActivity.this, MeOrderDetailsActivity.class);
            KLog.i("----ooo---->" + mList.get(position).getOrderId());

            intent.putExtra("orderId", mList.get(position).getOrderId());
            startActivity(intent);

//            int who = 0;    // 0:发生了异常     1：本人是买家     2:本人是卖家
//            int what = 0;   // 0:发生了异常     1：未发货  2：已发货  3：已完成  4：订单已取消

//            KLog.i("----lin---->  getuid" + mList.get(position).getBuyUser().getUid());
//            KLog.i("----lin---->  getuseruid" + Shared.getUserInfo().getUid());


//            if (mList.get(position).getBuyUser().getUid().equals(Shared.getUserInfo().getUid()))
//                who = 1;
//            if (!mList.get(position).getBuyUser().getUid().equals(Shared.getUserInfo().getUid()))
//                who = 2;
//
//            if (mList.get(position).getStatus().equals("yzf") || mList.get(position).getStatus().equals("yzjpxx") || mList.get(position).getStatus().equals("cj")) {
//                KLog.i("----lin----> 111111111111");
//                what = 1;
//            } else if (mList.get(position).getStatus().equals("jyz")) {
//                KLog.i("----lin----> 2222222222222");
//                what = 2;
//            } else if (mList.get(position).getStatus().equals("jywc")) {
//                KLog.i("----lin----> 3333333333333");
//                what = 3;
//            } else if (mList.get(position).getStatus().equals("qx")) {
//                KLog.i("----lin----> 44444444444444");
//                what = 4;
//            }
//
//            if (who == 1 && what == 1) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "11");
//                startActivity(intent);
//
//            }
//
//            if (who == 1 && what == 2) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "12");
//                startActivity(intent);
//            }
//
//            if (who == 1 && what == 3) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "13");
//                startActivity(intent);
//            }
//
//            if (who == 2 && what == 1) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "21");
//                startActivity(intent);
//            }
//
//            if (who == 2 && what == 2) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "22");
//                startActivity(intent);
//            }
//
//            if (who == 2 && what == 3) {
//                Intent intent = new Intent(MeOrderActivity.this, BuyerOrderDetailsActivity.class);
//                intent.putExtra("w", "23");
//                startActivity(intent);
//            }

//            Intent intent = new Intent(MeReward.this, BuyerOrderDetailsActivity.class);
//            Log.i("lin", "---->" + mlist.get(position).getWid());
//            intent.putExtra("wid", mlist.get(position).getWid());
//            startActivity(intent);
        }
    };

    protected void dialog2(AlertDialog.Builder builder) {
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setMessage("请登录");
        builder.show();

    }

    @OnClick(R.id.back_shipping_address)
    public void exit() {
        finish();
    }


}
