package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.ui.adapter.ManageAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/24.管理地址界面,
 */
public class ManageAddressActivity extends AppCompatActivity implements ManageAddressAdapter.OnTitleClickListener {

    private ManageAddressAdapter mAdapter;
    private List<AllAddress> list;
    @BindView(R.id.manage_address_recyclerView) RecyclerView mRecyclerView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);
        list = new ArrayList<AllAddress>();
        mAdapter = new ManageAddressAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter.setOnTitleClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);


    }

    @OnClick(R.id.back_manage_address)
    public void back() {

        finish();
    }

    @OnClick(R.id.manage_add_address)
    public void add() {
        Intent intent = new Intent(ManageAddressActivity.this, EditAddressActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final HttpResultListener<List<AllAddress>> listener;

        listener = new HttpResultListener<List<AllAddress>>() {
            @Override
            public void onSuccess(List<AllAddress> allAddresses) {
                Toast.makeText(ManageAddressActivity.this, "获取收货成功", Toast.LENGTH_SHORT).show();
                mAdapter.setList(allAddresses);
                list = allAddresses;

            }

            @Override
            public void onError(Throwable e) {
                Log.i("lin", "----lin----> 获取收货地址失败 " + e.toString());
            }
        };
       // ApiService5.getInstance().allAddress(listener, 1);
        A.getA(1,0,1).allAddress(listener,1);
    }

    @Override public void onTitleClick(String id) {
        Log.i("lin", "----lin----> onTitleClick 的 id " + id);
    }



}















