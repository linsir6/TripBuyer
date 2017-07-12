package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lin_sir_one.tripbuyer.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/9/23.交易条款
 */
public class TradeActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back_trade)
    public void back() {
        finish();
    }
}
