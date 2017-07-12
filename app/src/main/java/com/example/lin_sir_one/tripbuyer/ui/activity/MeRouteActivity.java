package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.event.FirstEvent;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.adapter.MeRouteAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by linSir on 16/8/5.我的旅游行程界面,
 */
public class MeRouteActivity extends AppCompatActivity {

    @BindView(R.id.rc_my_route) RecyclerView rc;
    @BindView(R.id.hint_me_route) TextView hint;
    private MeRouteAdapter mAdapter;
    private List<JourneyModel> mList;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_route);
        ButterKnife.bind(this);
        mAdapter = new MeRouteAdapter();
        rc.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));

        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(mAdapter);


        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeRouteActivity.this);
            dialog2(builder);
        }

        HttpResultListener<List<JourneyModel>> listener = new HttpResultListener<List<JourneyModel>>() {
            @Override public void onSuccess(List<JourneyModel> journeyModels) {
                KLog.i("========================成功");

                mList = journeyModels;

                if (journeyModels.size() == 0) {
                    hint.setVisibility(View.VISIBLE);
                }
                mAdapter.refresh(journeyModels);
            }

            @Override public void onError(Throwable e) {
                KLog.i("========================失败" + e.toString());
                hint.setVisibility(View.VISIBLE);
            }
        };
        A.getA(2, 0, 1).meRoute(listener, "1", "200");

    }

    @OnClick(R.id.back_me_route)
    public void back() {
        finish();
    }

    protected void dialog2(AlertDialog.Builder builder) {
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new FirstEvent("exit"));
                finish();
            }
        });
        builder.setMessage("请登录");
        builder.show();


    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            long nowTime = System.currentTimeMillis();
            long backTime = Long.parseLong(mList.get(position).getReturnDate());
            if (nowTime >= backTime) {
                Intent intent = new Intent(MeRouteActivity.this, MeRouteDetailsActivity.class);
                intent.putExtra("rid", mList.get(position).getRid());
                intent.putExtra("xinXi", "yiChuXing");
                startActivity(intent);
            } else {
                Intent intent = new Intent(MeRouteActivity.this, MeRouteDetailsActivity.class);
                intent.putExtra("rid", mList.get(position).getRid());
                intent.putExtra("xinXi", "weiChuXing");
                startActivity(intent);
            }

        }
    };

}
