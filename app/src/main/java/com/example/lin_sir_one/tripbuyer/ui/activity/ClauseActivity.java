package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lin_sir_one.tripbuyer.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/9/23.法律条款
 */
public class ClauseActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.trade_clause)
    public void tradeClause() {
        startActivity(new Intent(ClauseActivity.this, TradeActivity.class));
    }

    @OnClick(R.id.law_clause)
    public void law() {
        startActivity(new Intent(ClauseActivity.this, LawActivity.class));
    }

}
