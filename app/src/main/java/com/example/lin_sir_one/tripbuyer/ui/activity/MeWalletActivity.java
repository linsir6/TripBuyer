package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.model.WalletLogModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.adapter.MyWalletAdapter;
import com.socks.library.KLog;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/5.我的钱包界面,
 */
public class MeWalletActivity extends AppCompatActivity {

    @BindView(R.id.balance_me_wallet) TextView balance;
    @BindView(R.id.recyclerView_me_wallet) RecyclerView recyclerView;
    private MyWalletAdapter myWalletAdapter;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_wallet);

        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeWalletActivity.this);
            dialog2(builder);
        }

        ButterKnife.bind(this);
        myWalletAdapter = new MyWalletAdapter();
        recyclerView.setAdapter(myWalletAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initBlance();
        initLog();

    }

    private void initLog() {

        HttpResultListener<List<WalletLogModel>> listener = new HttpResultListener<List<WalletLogModel>>() {
            @Override public void onSuccess(List<WalletLogModel> walletLogModels) {
                myWalletAdapter.refresh(walletLogModels);
            }

            @Override public void onError(Throwable e) {

            }
        };

        A.getA(4, 0, 1).walletLog(listener, "1");

    }


    private void initBlance() {
        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {
                // KLog.i("____lin____> + " + String.valueOf(obj.getBalance()));
                KLog.i("____lin____> + 成功" + obj.getBalance());


                String money = String.valueOf(obj.getBalance());
                float scale = Float.valueOf(money);
                DecimalFormat fnum = new DecimalFormat("##0.00");
                String dd = fnum.format(scale);
                balance.setText(String.valueOf(dd + "(元)"));

            }

            @Override public void onError(Throwable e) {
                KLog.i("____lin____> + " + e.toString());
            }
        };

        A.getA(4, 0, 1).walltet(listener, "1");

    }

    @OnClick(R.id.back_wallet_activity)
    public void back() {
        finish();
    }

    /**
     * 提现
     */
    @OnClick(R.id.lin1_wallet)
    public void withdrawals() {
        startActivity(new Intent(MeWalletActivity.this, WithdrawActivity.class));
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

}
