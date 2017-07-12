package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.adapter.ShippingAddressAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/17.收货地址界面,
 */
public class ShippingAddressActivity extends AppCompatActivity {

    private TextView addAddress;
    private ImageView back;
    private RecyclerView mRecyclerView;
    private ShippingAddressAdapter mAdapter;
    private List<AllAddress> mAddress;
    private int temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        initViews();
        ButterKnife.bind(this);
    }

    private void initViews() {
        addAddress = (TextView) findViewById(R.id.add_address);
        back = (ImageView) findViewById(R.id.back_shipping_address);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_shipping_address);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShippingAddressAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //Toast.makeText(ShippingAddressActivity.this, "您点击的是：   " + position, Toast.LENGTH_SHORT).show();
            temp = position;

            Intent intent = new Intent();
            //把返回数据存入Intent

            intent.putExtra("userName", mAddress.get(position).getShipName());
            intent.putExtra("userPhone", mAddress.get(position).getPhone());

            String userAddress = mAddress.get(position).getProvince() + " " + mAddress.get(position).getCity()
                    + " " + mAddress.get(position).getArea() + " " + mAddress.get(position).getDetail();

            intent.putExtra("userAddress", userAddress);
            intent.putExtra("addressId", String.valueOf(mAddress.get(position).getAddressId()));
            //Toast.makeText(ShippingAddressActivity.this, "dddd  " + mAddress.get(position).getAddressId(), Toast.LENGTH_SHORT).show();

            //设置返回数据
            ShippingAddressActivity.this.setResult(1, intent);
            //关闭Activity
            ShippingAddressActivity.this.finish();

        }
    };

    public void back() {

        try {
            temp = 0;
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("userName", mAddress.get(0).getShipName());
            intent.putExtra("userPhone", mAddress.get(0).getPhone());

            String userAddress = mAddress.get(0).getProvince() + " " + mAddress.get(0).getCity()
                    + " " + mAddress.get(0).getArea() + " " + mAddress.get(0).getDetail();

            intent.putExtra("userAddress", userAddress);
            intent.putExtra("addressId", String.valueOf(mAddress.get(0).getAddressId()));

            //设置返回数据
            ShippingAddressActivity.this.setResult(1, intent);
            //关闭Activity
            ShippingAddressActivity.this.finish();
        } catch (Exception e) {
            Intent intent = new Intent();
            ShippingAddressActivity.this.setResult(6, intent);
            ShippingAddressActivity.this.finish();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        HttpResultListener<List<AllAddress>> listener;

        listener = new HttpResultListener<List<AllAddress>>() {
            @Override
            public void onSuccess(List<AllAddress> allAddresses) {
                Log.i("lin", "----lin----> 获取收货地址成功   " + allAddresses.size());
                Toast.makeText(ShippingAddressActivity.this, "成功", Toast.LENGTH_SHORT).show();
                mAdapter.clear();
                mAdapter.refresh(allAddresses);
                mAddress = allAddresses;
            }

            @Override
            public void onError(Throwable e) {
                Log.i("lin", "----lin----> 获取收货地址失败 " + e.toString());
            }
        };
        //ApiService5.getInstance().allAddress(listener, 1);
        A.getA(1, 0, 1).allAddress(listener, 1);
    }


    @OnClick(R.id.manage_shipping)
    public void manage() {

        Log.i("lin", "----lin---->  " + Shared.getCookie());

        if (Shared.getCookie() == null) {
            Toast.makeText(ShippingAddressActivity.this, "请登录后再管理地址", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(ShippingAddressActivity.this, Shared.getCookie(), Toast.LENGTH_SHORT).show();
            Log.i("lin", "----lin---->  " + Shared.getCookie());
            Intent intent = new Intent(ShippingAddressActivity.this, ManageAddressActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.add_address)
    public void add() {
        if (Shared.getCookie() == null) {
            Toast.makeText(ShippingAddressActivity.this, "请登录后再添加地址", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("lin", "----lin---->  " + Shared.getCookie());
            Intent intent = new Intent(ShippingAddressActivity.this, EditAddressActivity.class);
            startActivity(intent);
        }


    }


}
