package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lin_sir_one.tripbuyer.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/22.关于我们的界面
 */
public class AboutActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back_me_about)
    public void back() {
        finish();
    }
}
