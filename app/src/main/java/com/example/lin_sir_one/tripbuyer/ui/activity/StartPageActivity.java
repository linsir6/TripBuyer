package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;

/**
 * Created by linSir on 16/9/13.启动页
 */
public class StartPageActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab4);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Shared.getIsFirst().equals("0")) {
                    Shared.saveIsFirst("1");
                    Intent intent = new Intent(StartPageActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartPageActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 1300);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
