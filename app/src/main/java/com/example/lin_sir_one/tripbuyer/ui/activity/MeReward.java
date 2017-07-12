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
import com.example.lin_sir_one.tripbuyer.ui.adapter.MeRewardAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/21.我的悬赏令界面
 */
public class MeReward extends AppCompatActivity {

    @BindView(R.id.rc_my_reward) RecyclerView mRecyclerView;
    @BindView(R.id.hint_me_reward) TextView hint;

    private MeRewardAdapter mAdapter;
    private List<FamousPageModel> mlist;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_reward);

        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeReward.this);
            dialog2(builder);
        }


        ButterKnife.bind(this);
        mAdapter = new MeRewardAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        HttpResultListener<List<FamousPageModel>> listener = new HttpResultListener<List<FamousPageModel>>() {
            @Override public void onSuccess(List<FamousPageModel> famousPageModels) {
                if (famousPageModels.size() == 0)
                    hint.setVisibility(View.VISIBLE);

                mAdapter.refresh(famousPageModels);
                Log.i("lin", "---linSir--->" + famousPageModels.size());
                mlist = famousPageModels;

            }

            @Override public void onError(Throwable e) {

                hint.setVisibility(View.VISIBLE);

                Log.i("lin", "---linSir--->" + e.toString());
            }
        };

        A.getA(3, 0, 1).meReward(listener, "1", "200");
    }

    @OnClick(R.id.back_me_reward)
    public void back() {
        finish();
    }

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

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            if (mlist.get(position).getStatus().equals("dzf")) {
                Intent intent = new Intent(MeReward.this, MeRewardDetails.class);
                intent.putExtra("wid", mlist.get(position).getWid());
                intent.putExtra("xinXi", "dzf");
                startActivity(intent);
            }

            if (mlist.get(position).getStatus().equals("yzf")) {
                Intent intent = new Intent(MeReward.this, MeRewardDetails.class);
                intent.putExtra("wid", mlist.get(position).getWid());
                intent.putExtra("xinXi", "yzf");
                startActivity(intent);
            }

            if (mlist.get(position).getStatus().equals("jyz")) {
                Intent intent = new Intent(MeReward.this, MeRewardDetails.class);
                intent.putExtra("wid", mlist.get(position).getWid());
                intent.putExtra("xinXi", "jyz");
                startActivity(intent);
            }

            if (mlist.get(position).getStatus().equals("jywc")) {
                Intent intent = new Intent(MeReward.this, MeRewardDetails.class);
                intent.putExtra("wid", mlist.get(position).getWid());
                intent.putExtra("xinXi", "jywc");
                startActivity(intent);
            }


//            Intent intent = new Intent(MeReward.this, BuyerOrderDetailsActivity.class);
//            Log.i("lin", "---->" + mlist.get(position).getWid());
//            intent.putExtra("wid", mlist.get(position).getWid());
//            startActivity(intent);
        }
    };
}
