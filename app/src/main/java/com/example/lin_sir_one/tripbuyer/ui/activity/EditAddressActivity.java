package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/17.编辑地址的界面,
 */
public class EditAddressActivity extends AppCompatActivity {

    @BindView(R.id.talk_order_details) TextView save;
    @BindView(R.id.edit_name) EditText userName;
    @BindView(R.id.edit_tel) EditText userTel;
    @BindView(R.id.edit_province) EditText province;
    @BindView(R.id.edit_city) EditText city;
    @BindView(R.id.edit_area) EditText area;
    @BindView(R.id.edit_detail_area) EditText detail;
    HttpResultListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);

        listener = new HttpResultListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(EditAddressActivity.this, "保存地址成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }


    @OnClick(R.id.talk_order_details)
    public void save() {


        A.getA(1, 0, 1).address(listener, userName.getText().toString(), province.getText().toString()
                , city.getText().toString(), area.getText().toString(), detail.getText().toString()
                , userTel.getText().toString(), "0451", 0);

        finish();

    }
}
